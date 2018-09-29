package com.example.yunjin_choi.sqliteexam;

import android.content.Context;
import android.support.constraint.solver.widgets.Snapshot;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yunjin_choi.sqliteexam.dto.ProductDTO;
import com.example.yunjin_choi.sqliteexam.handler.MyHandler;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private TextView productID = null;
    private EditText productName = null;
    private EditText productQuantity = null;

    private Button productAdd = null;
    private Button productFind = null;
    private Button productDelete = null;

    private MyHandler myHandler = null;

    private Context mCtx = null;

    private View mView = null;

    private InputMethodManager imm = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initComponents();


    }


    //Component 초기화.
    public void initComponents(){

        mCtx = this;
        mView = (LinearLayout)findViewById(R.id.activity_layout);

        productID = (TextView) findViewById(R.id.product_id);
        productName = (EditText) findViewById(R.id.product_name);
        productQuantity = (EditText) findViewById(R.id.product_quantity);

        productAdd = (Button) findViewById(R.id.product_add);
        productFind = (Button) findViewById(R.id.product_find);
        productDelete = (Button) findViewById(R.id.product_delete);

        productAdd.setOnClickListener(this);
        productFind.setOnClickListener(this);
        productDelete.setOnClickListener(this);

        myHandler = new MyHandler(this , null , null , 1);

        imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
    }

    @Override
    public void onClick(View view) {
        hideKeyboard();
        switch (view.getId()){

            case R.id.product_add :

                addProduct();

                break;

            case R.id.product_find :

                findProduct();

                break;

            case R.id.product_delete :

                deleteProduct();

                break;
        }
    }

    public void addProduct(){

        String quantityText = productQuantity.getText().toString();
        int quantity = "".equals(quantityText) ? 0 : Integer.parseInt(quantityText) ;

        myHandler.insertProduct(new ProductDTO(productName.getText().toString() , quantity));

        productName.setText("");
        productQuantity.setText("");

    }

    public void findProduct(){

        ProductDTO product = myHandler.findProductByName(productName.getText().toString());

        if(product != null) {
            productID.setText(String.valueOf(product.getProductID()));
            productQuantity.setText(String.valueOf(product.getProductQuantity()));

        }else{
            Snackbar.make(mView , "해당되는 데이터가 없습니다! . " , Snackbar.LENGTH_LONG).show();
            productName.setText("");
            productQuantity.setText("");
        }
    }

    public void deleteProduct(){

        int result = myHandler.deleteProductByName(productName.getText().toString());

        if( result > 0){
            Snackbar.make(mView , "해당되는 데이터가 삭제 되었습니다! . " , Snackbar.LENGTH_LONG).show();

        }else {
            Snackbar.make(mView , "해당되는 데이터가 없습니다! . " , Snackbar.LENGTH_LONG).show();
        }
        productName.setText("");
        productQuantity.setText("");
    }

    private void hideKeyboard()
    {
        imm.hideSoftInputFromWindow(productAdd.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(productFind.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(productDelete.getWindowToken(), 0);
    }
}
