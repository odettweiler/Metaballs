Metaball[] metaballs = new Metaball[5];
int resFactor = 10;
int drawMode;
boolean centerAttract = true;
int radiusMult = 1;

void setup() {
  size(600, 600);
  frameRate(50);
  colorMode(HSB);
  
  for (int i = 0; i < metaballs.length; i++) {
    metaballs[i] = new Metaball(random(width), random(height), random(20, 30));
  }
}

void draw() {
  drawInterference();
  drawMetaBalls();
  
  println(frameRate);
}

void drawMetaBalls() {
  for (Metaball m : metaballs) {
    m.update();
    m.drawSelf();
  }
}

float getInterferenceAtPoint(int px, int py) {
  float sum = 0.0;
  for (Metaball m : metaballs) {
    sum += m.getInterference(px, py);
  }
  
  return sum;
}

void drawInterference() {
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

void drawPixel(int x, int y, float hu) {
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

void keyPressed() {
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

void mousePressed() {
  if (mouseButton == LEFT) {
    centerAttract = !centerAttract;
  }
}
