package sala.fr;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

public class Dialog  extends AppCompatDialogFragment {

    private EditText editNewCateg;
    private DialogListener listener;

    @Nullable
    @Override
    public android.app.Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.layout_dialog, null);
        builder.setView(view).setTitle("Nouvelle catégorie").setNegativeButton("Retour", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        }).setPositiveButton("Validé", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                String textNewCateg = editNewCateg.getText().toString();
                listener.addToCateg(textNewCateg);
                Toast.makeText(getContext(), "Catégorie ajoutée !", Toast.LENGTH_SHORT).show();

                ((Activity) getContext()).finish();
                /*((Activity) getContext()).overridePendingTransition(0, 0);*/
                startActivity(((Activity) getContext()).getIntent());
                /*((Activity) getContext()).overridePendingTransition(0, 0);*/
            }
        });

        editNewCateg = view.findViewById(R.id.newCategInput);

        return builder.create();
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        try {
            listener  = (DialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + "must implement DialogListener");
        }
    }

    public interface DialogListener{
        void addToCateg(String newCateg);

    }


}
