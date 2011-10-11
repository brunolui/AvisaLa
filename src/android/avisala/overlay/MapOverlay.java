package android.avisala.overlay;

import java.util.ArrayList;

import android.avisala.objetos.Rota;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapController;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;

public class MapOverlay extends Overlay {

	Rota rotaMapa;
    ArrayList<GeoPoint> pontos;

    public MapOverlay(Rota rota, MapView mapa) {
    	rotaMapa = rota;
        if (rota.coordenadas.length > 0) {
        	pontos = new ArrayList<GeoPoint>();
        	
            for (int i = 0; i < rota.coordenadas.length; i++) {
            	pontos.add(new GeoPoint((int) (rota.coordenadas[i][1] * 1000000),(int) (rota.coordenadas[i][0] * 1000000)));
            }
            int moveToLat = (pontos.get(0).getLatitudeE6() + (pontos.get(pontos.size() - 1).getLatitudeE6() - pontos.get(0).getLatitudeE6()) / 2);
            int moveToLong = (pontos.get(0).getLongitudeE6() + (pontos.get(pontos.size() - 1).getLongitudeE6() - pontos.get(0).getLongitudeE6()) / 2);
            GeoPoint moveTo = new GeoPoint(moveToLat, moveToLong);

            MapController mapController = mapa.getController();
            mapController.setZoom(15);
            mapController.animateTo(moveTo);
        }
    }

    @Override
    public boolean draw(Canvas canvas, MapView mv, boolean shadow, long when) {
    	super.draw(canvas, mv, shadow);
        drawPath(mv, canvas);
        return true;
    }

    public void drawPath(MapView mv, Canvas canvas) {
        int x1 = -1, y1 = -1, x2 = -1, y2 = -1;
        Paint paint = new Paint();
        paint.setColor(Color.MAGENTA);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(3);
        
        for (int i = 0; i < pontos.size(); i++) {
        	Point point = new Point();
            mv.getProjection().toPixels(pontos.get(i), point);
            x2 = point.x;
            y2 = point.y;
            if (i > 0) {
            	canvas.drawLine(x1, y1, x2, y2, paint);
            }
            x1 = x2;
            y1 = y2;
        }
    }

}