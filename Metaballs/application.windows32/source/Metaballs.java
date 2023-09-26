import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class Metaballs extends PApplet {

Metaball[] metaballs = new Metaball[5];
int resFactor = 10;
int drawMode;
boolean centerAttract = true;
int radiusMult = 1;

public void setup() {
  
  frameRate(50);
  colorMode(HSB);
  
  for (int i = 0; i < metaballs.length; i++) {
    metaballs[i] = new Metaball(random(width), random(height), random(20, 30));
  }
}

public void draw() {
  drawInterference();
  drawMetaBalls();
  
  println(frameRate);
}

public void drawMetaBalls() {
  for (Metaball m : metaballs) {
    m.update();
    m.drawSelf();
  }
}

public float getInterferenceAtPoint(int px, int py) {
  float sum = 0.0f;
  for (Metaball m : metaballs) {
    sum += m.getInterference(px, py);
  }
  
  return sum;
}

public void drawInterference() {
  //loadPixels();
  for (int x = 0; x < width; x+=resFactor) {
    for (int y = 0; y < height; y+=resFactor) {
      float interference = getInterferenceAtPoint(x, y);
      float hu = map(interference, 0, metaballs.length, 0, 255);
      drawPixel(x, y, hu);
    }
  }
  //updatePixels();
}

public void drawPixel(int x, int y, float hu) {
  switch (drawMode) {
    case 0:
      stroke(hu, 255, 255);
      fill(hu, 255, 255);
      //pixels[(width*y)+x] = color(hu, 255, 255);
      rect(x, y, resFactor, resFactor);
      return;
    case 1:
      stroke(0, 0, hu);
      fill(0, 0, hu);
      //pixels[(width*y)+x] = color(0, 0, hu);
      rect(x, y, resFactor, resFactor);
      return;
    case 2:
      //color col;
      if (hu > 50 ) {
        //col = color(0, 0, 255);
        stroke(0, 0, 255);
        fill(0, 0, 255);
      } else {
        //col = color(0, 0, 0);
        stroke(0, 0, 0);
        fill(0, 0, 0);
      }
      //pixels[(width*y)+x] = col;
      rect(x, y, resFactor, resFactor);
      return;
  }
}

public void keyPressed() {
  if (key == '1') {
    drawMode = 0;
    radiusMult = 1;
  } else if (key == '2') {
    drawMode = 1;
    radiusMult = 3;
  } else if (key == '3') {
    drawMode = 2;
    radiusMult = 1;
  }
}

public void mousePressed() {
  if (mouseButton == LEFT) {
    centerAttract = !centerAttract;
  }
}
public class Metaball {
  public float x;
  public float y;
  
  public float radius;
  
  PVector vel;
  
  public Metaball(float x_, float y_, float r) {
    this.x = x_;
    this.y = y_;
    this.radius = r;
    
    this.vel = new PVector(random(-0.5f, 0.5f), random(-0.5f, 0.5f));
  }
  
  // return distance based on distance formula
  public float getDistFromPoint(int px, int py) {
    return sqrt(pow((px-this.x), 2) + pow((py-this.y), 2)) / (this.radius*radiusMult);
  }
  
  // return constructive interference
  public float getInterference(int px, int py) {
    return 1/ getDistFromPoint(px, py); // should be 1/distance
  }
  
  public void update() {    
    if (this.x+vel.x < 0) {
      vel.x *= -1;
    } else if (this.x+vel.x > width) {
      vel.x *= -1;
    }
    if (this.y+vel.y < 0) {
      vel.y *= -1;
    } else if (this.y+vel.y > height) {
      vel.y *= -1;
    }
    
    this.x += vel.x * 5;
    this.y += vel.y * 5;
    
    vel.x += random(-0.05f, 0.05f);
    vel.y += random(-0.05f, 0.05f);
    
    for (Metaball m : metaballs) {
      if (m != this) {
        repelFrom(m);
      }
    }
    if (centerAttract) attractToCenter();
    
    clampVel();
  }
  
  public void clampVel() {
    if (vel.x > 0.5f) {
      vel.x = 0.5f;
    } else if (vel.x < -0.5f) {
      vel.x = -0.5f;
    }
    if (vel.y > 0.5f) {
      vel.y = 0.5f;
    } else if (vel.y < -0.5f) {
      vel.y = -0.5f;
    }
  }
  
  public void drawSelf() {
    stroke(0, 0, 255);
    fill(0, 0, 255, 100);
    ellipse(this.x, this.y, 10, 10);
  }
  
  public PVector getCenterOfMass() {
    float xSum = 0;
    float ySum = 0;
    for (Metaball m : metaballs) {
      xSum += m.x;
      ySum += m.y;
    }
    
    PVector center = new PVector(xSum/metaballs.length, ySum/metaballs.length);
    return center;
  }
  
  // do this for each other metaball
  public void repelFrom(Metaball m) {
    float interference = m.getInterference((int) this.x, (int) this.y) / 50;
    PVector dir = new PVector((this.x - m.x), (this.y - m.y));
    dir.normalize();
    dir.mult(interference);
    
    vel.add(dir);
  }
  
  public void attractToCenter() {
    PVector dir = new PVector(((width/2) - this.x), ((height/2) - this.y));
    dir.normalize();
    dir.mult(0.025f);
    
    vel.add(dir);
  }
}
  public void settings() {  size(600, 600); }
  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "Metaballs" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
