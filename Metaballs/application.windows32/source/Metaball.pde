public class Metaball {
  public float x;
  public float y;
  
  public float radius;
  
  PVector vel;
  
  public Metaball(float x_, float y_, float r) {
    this.x = x_;
    this.y = y_;
    this.radius = r;
    
    this.vel = new PVector(random(-0.5, 0.5), random(-0.5, 0.5));
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
    
    vel.x += random(-0.05, 0.05);
    vel.y += random(-0.05, 0.05);
    
    for (Metaball m : metaballs) {
      if (m != this) {
        repelFrom(m);
      }
    }
    if (centerAttract) attractToCenter();
    
    clampVel();
  }
  
  public void clampVel() {
    if (vel.x > 0.5) {
      vel.x = 0.5;
    } else if (vel.x < -0.5) {
      vel.x = -0.5;
    }
    if (vel.y > 0.5) {
      vel.y = 0.5;
    } else if (vel.y < -0.5) {
      vel.y = -0.5;
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
    dir.mult(0.025);
    
    vel.add(dir);
  }
}
