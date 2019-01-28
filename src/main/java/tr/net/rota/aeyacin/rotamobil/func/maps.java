package tr.net.rota.aeyacin.rotamobil.func;

import android.annotation.SuppressLint;
import android.view.View;
import android.view.ViewTreeObserver;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLngBounds;

/**
 * Created by ayacin on 21.03.2017.
 */

public class maps {

    public void mapviewallmarkerauto(final View mapView , final GoogleMap mMap){
       // final View mapView = getSupportFragmentManager().findFragmentById( R.id.map).getView();
        if (mapView.getViewTreeObserver().isAlive()) {
            mapView.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @SuppressLint("NewApi")
                @Override
                public void onGlobalLayout() {
                    LatLngBounds.Builder bld = new LatLngBounds.Builder();
//                    for (int i = 0; i < YOUR_ARRAYLIST.size(); i++) {
//                        LatLng ll = new LatLng(YOUR_ARRAYLIST.get(i).getPos().getLat(), YOUR_ARRAYLIST.get(i).getPos().getLon());
//                        bld.include(ll);
//                    }
                    LatLngBounds bounds = bld.build();
                    mMap.moveCamera( CameraUpdateFactory.newLatLngBounds(bounds, 70));
                    mapView.getViewTreeObserver().removeGlobalOnLayoutListener(this);

                }
            });
        }
    }
}
