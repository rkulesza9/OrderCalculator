package illeagle99.ordercalculator;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import illeagle99.ordercalculator.backend.Item;
import illeagle99.ordercalculator.backend.Receipt;

public class ItemActivity extends AppCompatActivity {
    private Receipt receipt;
    private boolean isNew = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);
        receipt = (Receipt) getIntent().getBundleExtra("receipt").getBinder("receipt");
        if(receipt.getSelected() == null){
            Item item = new Item();
            receipt.select(item);
            isNew = true;
        }

        setText();
        setTypeListener();
    }

    private void setTypeListener(){
        EditText name = (EditText) findViewById(R.id.editText);
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                ((TextView) findViewById(R.id.textView3)).setText(editable.toString());
            }
        });
    }

    public void save(View v){
        String name = ((TextView) findViewById(R.id.textView3)).getText().toString();
        receipt.getSelected().setName(name);
        receipt.getSelected().setTaxable(((CheckBox) findViewById(R.id.checkBox)).isChecked());
        double cost = handleUserStupidity(((EditText) findViewById(R.id.editText2)).getText().toString());
        if(cost == -1){
            Toast.makeText(this,"input not understood", Toast.LENGTH_SHORT).show();
            return;
        }
        receipt.getSelected().setCost(cost);
        if(isNew){
            receipt.add(receipt.getSelected());
            isNew = false;
        }
        Toast.makeText(this,"item saved!",Toast.LENGTH_SHORT).show();
    }

    private double handleUserStupidity(String input){
        try {
            Double dInput = Double.parseDouble(input);
            return dInput;
        }catch(Exception e) {
            return -1;
        }
    }

    private void setText(){
        String price = String.format("%.2f",receipt.getSelected().getCost());
        ((TextView) findViewById(R.id.textView3)).setText(receipt.getSelected().getName());
        ((EditText) findViewById(R.id.editText)).setText(receipt.getSelected().getName());
        ((EditText) findViewById(R.id.editText2)).setText(price);
        ((CheckBox) findViewById(R.id.checkBox)).setChecked(receipt.getSelected().isTaxable());
    }
}
