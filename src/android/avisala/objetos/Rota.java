package android.avisala.objetos;


public class Rota {

	public String nome;
    public String descricao;
    public int cor;
    public int largura;
    public double[][] coordenadas = new double[][] {};
    public Destino[] pontos = new Destino[] {};
	
	private double latitudeOrigem;
	private double longitudeOrigem;
	private double latitudeDestino;
	private double longitudeDestino;

	
	public double getLatitudeOrigem() {
		return latitudeOrigem;
	}

	public void setLatitudeOrigem(double latitudeOrigem) {
		this.latitudeOrigem = latitudeOrigem;
	}

	public double getLongitudeOrigem() {
		return longitudeOrigem;
	}

	public void setLongitudeOrigem(double longitudeOrigem) {
		this.longitudeOrigem = longitudeOrigem;
	}

	public double getLatitudeDestino() {
		return latitudeDestino;
	}

	public void setLatitudeDestino(double latitudeDestino) {
		this.latitudeDestino = latitudeDestino;
	}

	public double getLongitudeDestino() {
		return longitudeDestino;
	}

	public void setLongitudeDestino(double longitudeDestino) {
		this.longitudeDestino = longitudeDestino;
	}

	public boolean naoPossuiCoordenadasDeOrigem() {
		return latitudeOrigem == 0 && longitudeOrigem == 0;
	}

}
