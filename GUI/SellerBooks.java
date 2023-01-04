package GUI;

public class SellerBooks {
	private String name;
	private float actualPrize;
	private float aumento;
	
	public SellerBooks(String name, float actualPrize, float aumento) {
		this.name = name;
		this.actualPrize = actualPrize;
		this.aumento = aumento;
	}
	
	public String getName() {
		return this.name;
	}
	
	public float getActualPrize () {
		return this.actualPrize;
	}

	public float getAumento() {
		return this.aumento;
	}

	public void setActualPrize (float precio) {
		this.actualPrize = precio;
	}
}
