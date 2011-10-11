package android.avisala.objetos;

public class Destino {

	public int id;
	public double latitude;
	public double longitude;
	public String endereco;
	public String nome;
	public String iconUrl;

    public Destino() {
	}
    
	public Destino(int id, String endereco, double latitude, double longitude) {
		this.id = id;
		this.endereco = endereco;
		this.latitude = latitude;
		this.longitude = longitude;
	}
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

}