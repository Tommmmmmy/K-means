public class Point {
	private int value;
	private int class_value;
	
	public Point(int value){
		this.value = value;
	}
	
	public int getValue(){
		return this.value;
	}
	
	public int getClassValue(){
		return this.class_value;
	}
	
	public void setClassValue(int class_value){
		this.class_value = class_value;
	}
	
	public int getAlpha(){
		return (this.value >> 24) & 0xFF;
	}
	
	public int getRed(){
		return (this.value >> 16) & 0xFF;
	}
	
	public int getGreen(){
		return (this.value >> 8) & 0xFF;
	}
	
	public int getBlue(){
		return this.value & 0xFF;
	}
	
	public int getAlpha(int value){
		return (value >> 24) & 0xFF;
	}
	
	public int getRed(int value){
		return (value >> 16) & 0xFF;
	}
	
	public int getGreen(int value){
		return (value >> 8) & 0xFF;
	}
	
	public int getBlue(int value){
		return value & 0xFF;
	}
}
