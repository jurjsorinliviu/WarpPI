package org.warp.picalculator.extra.mario;

public class MarioEntity {
	protected double x;
	protected double y;
	public double forceX;
	public double forceY;
	public boolean collisionUp;
	public boolean collisionDown;
	public boolean collisionLeft;
	public boolean collisionRight;
	public boolean subjectToGravity;
	
	public MarioEntity(double x, double y, double forceX, double forceY, boolean onGround, boolean subjectToGravity) {
		this.x = x;
		this.y = y;
		this.forceX = forceX;
		this.forceY = forceY;
		this.collisionDown = onGround;
		this.subjectToGravity = subjectToGravity;
	}

	public void setPosition(double x, double y) {
		this.x = x;
		this.y = y;
	}

	public void setPosition(double x, double y, boolean onGround) {
		this.x = x;
		this.y = y;
		this.collisionDown = onGround;
	}
	
	public double getX() {
		return x;
	}
	
	public double getY() {
		return y;
	}
	
	public boolean isOnGround() {
		return collisionDown;
	}
	
	public void setOnGround(boolean onGround) {
		this.collisionDown = onGround;
	}
	
	public void gameTick(double dt) {
		this.x = computeFutureDX(dt);
		this.y = computeFutureDY(dt);
		this.forceX = computeFutureForceDX(dt);
		this.forceY = computeFutureForceDY(dt);
	}
	
	public double computeFutureDX(double dt) {
		return (x + dt * forceX) - this.x;
	}
	
	public double computeFutureDY(double dt) {
		double forceY = this.forceY;
		double y = this.y;
		if (!collisionDown) {
			y += dt * forceY;
		}
		return y - this.y;
	}
	
	public double computeFutureForceDX(double dt) {
		double forceX = this.forceX;
		forceX *= 0.75;
		return forceX - this.forceX;
	}
	
	public double computeFutureForceDY(double dt) {
		double forceY = this.forceY;
		if (subjectToGravity && !this.collisionDown) {
			forceY -= dt * 1569.6/16f;
		} else {
			forceY *= 0.75;
		}
		return forceY - this.forceY;
	}
}
