package android.avisala.objetos;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.location.Address;
import android.location.Geocoder;

/**
 * Ajuda a construir um GeoPoint
 */
public class Endereco {

	private final Address address;

	public Endereco(Address address) {
		this.address = address;
	}

	public static List<Endereco> buscar(Context context, String rua) {
		Geocoder geocoder = new Geocoder(context, new Locale("pt","BR"));
		List<Address> addresses = null;
		try {
			addresses = geocoder.getFromLocationName(rua, 10);
			if(addresses != null){
				List<Endereco> enderecos = new ArrayList<Endereco>();
				for (Address a : addresses) {
					enderecos.add(new Endereco(a));
				}
				return enderecos;
			}
		} catch (IOException e) {
			System.out.println("Deu erro o geo coder - " + e.getMessage());
		}
		return null;
	}

	public String getEstado() {
		return address.getAdminArea();
	}

	public String getCidade() {
		return address.getLocality();
	}

	public String getRua() {
		if (address.getThoroughfare() != null && address.getThoroughfare() != "") {
			return address.getThoroughfare();			
		} else {
			return address.getFeatureName();
		}
	}

	public String getEnderecoCompleto() {
		if (getRua() != null && getCidade() != null && getEstado() != null) {
			return getRua() + ", " + getCidade() + " - " + getEstado();
		}
		return "";
	}

	public double getLatitude() {
		return address.getLatitude();
	}

	public double getLongitude() {
		return address.getLongitude();
	}
}