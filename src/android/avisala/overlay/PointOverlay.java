package android.avisala.overlay;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.RectF;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
/**
 * Simples Overlay que recebe as coordenadas Latitude e Longitude e mostra a imagem setada
 */
public class PointOverlay extends Overlay {
	
	private int imageId;
	private Paint paint = new Paint();
	private GeoPoint geoPoint;
	private RectF retangulo;

	public PointOverlay(GeoPoint geoPoint, int imageId) {
		this.geoPoint = geoPoint;
		this.imageId = imageId;
	}

	@Override
	public void draw(Canvas canvas, MapView mapView, boolean shadow) {
		super.draw(canvas, mapView, shadow);

		// Converte as coordenadas para pixels
		Point point = mapView.getProjection().toPixels(geoPoint, null);
		Bitmap bitmap = BitmapFactory.decodeResource(mapView.getResources(), this.imageId);
		retangulo = new RectF(point.x, point.y, point.x + bitmap.getWidth(), point.y + bitmap.getHeight());

		canvas.drawBitmap(bitmap, null, retangulo, paint);
	}

	public void setGeoPoint(GeoPoint geoPoint) {
		this.geoPoint = geoPoint;
	}

//	@Override
//	public boolean onTap(final GeoPoint geoPoint, MapView mapView) {
//		Point pontoEmPixels = mapView.getProjection().toPixels(geoPoint, null);
//		boolean pontoEstaContidoNoRetangulo = retangulo.contains(pontoEmPixels.x, pontoEmPixels.y);
//
//		if (pontoEstaContidoNoRetangulo) {
//			String[] opcoes = new String[] {"Salvar destino"};
//			
//			AlertDialog create = new AlertDialog.Builder(mapView.getContext())
//	        .setItems(opcoes, new DialogInterface.OnClickListener() {
//	            public void onClick(DialogInterface dialog, int id) {
//	            	salvarDestino(geoPoint);
//	            }
//				
//	        }).create();
//			create.show();
//			return true;
//		}
//		return super.onTap(geoPoint, mapView);
//	}

}
