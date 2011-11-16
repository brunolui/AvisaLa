package android.avisala.objetos;

/**
 *  Endereço inicial do mapa (Av. Paulista)
 *  
 *  android:apiKey="0vrcwTwEiGEnZl2WVi24BJNTkeHVze2S53NrACA"
 */
public class CoordenadaInicial extends Coordenada {

	public CoordenadaInicial() {
		super(-23.565029, -46.651953);
	}
	
	@Override
	public boolean isInicial() {
		return true;
	}
	
}
