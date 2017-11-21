package autostalker.bananaforscale.com.autostalker.Activities;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

/**
 * Created by Emiliano on 19/11/2017.
 */

public class FragmentDialog extends android.support.v4.app.DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the Builder class for convenient dialog construction
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setMessage("Quiere ver el video o compartirlo?")
                .setPositiveButton("Ver", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(getContext(), "Abriendo ultimo viaje", Toast.LENGTH_SHORT)
                                .show();
                        Intent myIntent = new Intent(getContext(), LastDriveActivity
                                .class);
                        getContext().startActivity(myIntent);
                    }
                })
                .setNegativeButton("Compartir", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        // User cancelled the dialog
                    }
                });
        // Create the AlertDialog object and return it
        return builder.create();
    }
}
