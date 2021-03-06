package com.martin.mvc.legaltalk;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.litepal.LitePal;
import org.litepal.crud.DataSupport;

import java.util.List;


public class MainActivity extends AppCompatActivity {


    Button loginBt;
    Button registBt;
    EditText usernameEt;
    EditText passwordEt;
    Context context;
    TextView goRegistTv;
    final String[] goRegistItems = new String[]{"用户注册","律师注册"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        context = this;
        //初始化界面信息
        init();
    }

    /**
     * 沉浸式布局
     */
    public void setFullScreen(){
        //隐藏状态栏
        View decorView6 = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= 21) {
            int uiOptions6 = View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView6.setSystemUiVisibility(uiOptions6);
        }
        //
        getSupportActionBar().hide();
    }


    @Override
    protected void onPostResume() {
        super.onPostResume();
        setFullScreen();
    }


    /**
     * 初始化操作
     */
    public void init(){

        setFullScreen();

        loginBt = findViewById(R.id.loginBt);
        registBt = findViewById(R.id.loginBt);
        usernameEt = findViewById(R.id.usernameEt);
        passwordEt = findViewById(R.id.passwordEt);
        goRegistTv = findViewById(R.id.goRegistTv);

        LitePal.getDatabase();

        //监听登录
        loginBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        //监听注册
        registBt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                regist();
            }
        });


        //监听选择注册
        goRegistTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goRegist();
            }
        });
    }

    /**
     * 注册选择 律师还是普通用户
     */
    private void goRegist() {
       new AlertDialog.Builder(context).setSingleChoiceItems(goRegistItems, 0, new DialogInterface.OnClickListener() {
           @Override
           public void onClick(DialogInterface dialogInterface, int i) {
               switch (i){
                   case 0:
                       //进入用户注册
                       break;
                   case 1:
                       //进入律师注册
                       break;
                   default:
                       break;
               }
           }
       }).create().show();
    }


    /**
     * 登录
     */
    public void login(){
        //合法性检查
        int uLength = usernameEt.getText().toString().length();
        int pLength = passwordEt.getText().toString().length();
        if(uLength<3||uLength>5){
            Toast.makeText(context,"用户名输入有误", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pLength<6||pLength>12){
            Toast.makeText(context,"密码输入有误", Toast.LENGTH_SHORT).show();
            return;
        }
        //是否已存在
        List<User> users = DataSupport.select()
                .where("username = ?",usernameEt.getText().toString())
                .find(User.class);

        if (users.size()>0){
            //检查密码
            if(users.get(0).getPassword() != null){
                if (users.get(0).getPassword().equals(passwordEt.getText().toString())) {
                    Toast.makeText(context, "登录成功，请稍后...", Toast.LENGTH_SHORT).show();
                    //跳转至用户信息界面
                    Intent intent = new Intent(context, RegistActivity.class);
                    startActivity(intent);
                    this.finish();
                }
            }else {
                Toast.makeText(context, users.get(0).getUsername(), Toast.LENGTH_SHORT).show();
                return;
            }
        }else {
            Toast.makeText(context,"用户不存在", Toast.LENGTH_SHORT).show();
            return;
        }
    }

    /**
     * 注册
     */
    public void regist(){
        //合法性检查
        int uLength = usernameEt.getText().toString().length();
        int pLength = passwordEt.getText().toString().length();
        if(uLength<3||uLength>5){
            Toast.makeText(context,"用户名应为长度为3-5的字符", Toast.LENGTH_SHORT).show();
            return;
        }
        if (pLength<6||pLength>12){
            Toast.makeText(context,"密码应不小于6位且不大于12位", Toast.LENGTH_SHORT).show();
            return;
        }
        //是否已存在
        List<User> users = DataSupport.select()
                                      .where("username = ?",usernameEt.getText().toString())
                                      .find(User.class);
        if (users.size()>0){
            Toast.makeText(context,"该账户已注册！", Toast.LENGTH_SHORT).show();
            return;
        }

        //注册成功，存入数据库
        User user = new User();
        user.setUsername(usernameEt.getText().toString());
        user.setPassword(passwordEt.getText().toString());
        user.save();
        Toast.makeText(context,"注册成功，请稍后...", Toast.LENGTH_SHORT).show();

        //跳转至用户信息界面
        Intent intent = new Intent(context,RegistActivity.class);
        startActivity(intent);
        this.finish();
    }
}
