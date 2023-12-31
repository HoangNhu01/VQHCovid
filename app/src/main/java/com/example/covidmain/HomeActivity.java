package com.example.covidmain;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.example.covidmain.API.ApiService;
import com.example.covidmain.Model.Covid;
import com.google.android.material.navigation.NavigationView;

import java.text.NumberFormat;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    Toolbar toolbar;
    DrawerLayout drawerLayout;
    NavigationView navigationView;
    ListView listView;
    TextView text_canhiem, text_tuvong, text_hoiphuc, text_dieutri;
    ConstraintLayout layout1;
    Button suckhoe, yte, vietnam, thegioi;
    ProgressBar spinner;

    String check = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        anhxa();
        actionToolBar();

        //khai báo sự kiện onclick từng item menu ->public boolean onNavigationItemSelected
        navigationView.setNavigationItemSelectedListener(this);

        //API

        callApi(0);
        vietnam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApi(0);
                check = "0";
            }
        });
        thegioi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                callApi(1);
                check = "1";
            }
        });

        //click xem biểu đồ
        layout1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, Chartcovid.class);
                intent.putExtra("check_input", check);
                startActivity(intent);
            }
        });
        //click xem chi tiet suc khoe
        suckhoe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, PreventionCovidActivity.class);
                startActivity(intent);
            }
        });

        yte.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(HomeActivity.this, NewsActivity.class);
                startActivity(intent);
            }
        });
    }

    //lay gia tri tu api
    private void callApi(int i) {
        // tạo 1 NumberFormat để định dạng số theo tiêu chuẩn của nước Anh
        Locale localeEN = new Locale("en", "EN");
        NumberFormat en = NumberFormat.getInstance(localeEN);
        spinner.setVisibility(View.VISIBLE);

        if (i == 0) {
            ApiService.apiService.convertapitovn("").enqueue(new Callback<Covid>() {
                @Override
                public void onResponse(Call<Covid> call, Response<Covid> response) {
                    //ánh xạ
                    Covid covid = response.body();
                    if (covid != null) {


                        text_canhiem.setText(en.format(covid.getCases()) + "");
                        text_dieutri.setText(en.format(covid.getActive()) + "");
                        text_hoiphuc.setText(en.format(covid.getRecovered()) + "");
                        text_tuvong.setText(en.format(covid.getDeaths()) + "");
                        spinner.setVisibility(View.GONE);
                        Toast.makeText(HomeActivity.this, "Lấy dữ liệu thành công", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Covid> call, Throwable t) {
                    Toast.makeText(HomeActivity.this, "Lỗi Lấy dữ liệu covid việt nam", Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            ApiService.apiService.convertapitoall("").enqueue(new Callback<Covid>() {
                @Override
                public void onResponse(Call<Covid> call, Response<Covid> response) {
                    Covid covid = response.body();
                    if (covid != null) {
                        text_canhiem.setText(en.format(covid.getCases()) + "");
                        text_dieutri.setText(en.format(covid.getActive()) + "");
                        text_hoiphuc.setText(en.format(covid.getRecovered()) + "");
                        text_tuvong.setText(en.format(covid.getDeaths()) + "");
                        spinner.setVisibility(View.GONE);
                        Toast.makeText(HomeActivity.this, "Lấy dữ liệu thành công", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<Covid> call, Throwable t) {
                    spinner.setVisibility(View.GONE);
                    Toast.makeText(HomeActivity.this, "Lỗi Lấy dữ liệu covid thế giới", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    //ham su kien thi an nut tro ve
    @Override
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    // khoi tao thu vien ho tro toolbar va  onclick
    private void actionToolBar() {
        // lấy đối tượng ActionBar sau đó bạn truy cập các thuộc tính
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //set icon
        toolbar.setNavigationIcon(R.drawable.ic_toolbar_menu);

        //bat su kien onclick
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });
    }


    private void anhxa() {
        toolbar = (Toolbar) findViewById(R.id.toolbar_menu);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.navigation_menu);
        text_canhiem = (TextView) findViewById(R.id.textView3);
        text_dieutri = (TextView) findViewById(R.id.textView12);
        text_hoiphuc = (TextView) findViewById(R.id.textView15);
        text_tuvong = (TextView) findViewById(R.id.textView9);
        layout1 = (ConstraintLayout) findViewById(R.id.lout_1);
        suckhoe = (Button) findViewById(R.id.buttonsuckhoe);
        yte = (Button) findViewById(R.id.buttonyte);
        vietnam = (Button) findViewById(R.id.vietnam_api);
        thegioi = (Button) findViewById(R.id.thegioi_api);
        spinner = (ProgressBar)findViewById(R.id.progressBar1);

    }

    // hàm sử lí sự kiện khi click vào menu item
    @Override
    public boolean onNavigationItemSelected(@NonNull @org.jetbrains.annotations.NotNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.mask_main) {
            Intent intent = new Intent(HomeActivity.this, PreventionCovidActivity.class);
            startActivity(intent);
        } else if (id == R.id.tintuc_main) {
            Intent intent = new Intent(HomeActivity.this, NewsActivity.class);
            startActivity(intent);
        }
        else if (id == R.id.About_main) {
            //Toast.makeText(HomeActivity.this,"mask click",Toast.LENGTH_LONG).show();
            Intent intent = new Intent(HomeActivity.this, AboutActivity.class);
            startActivity(intent);

        }else if (id == R.id.sp_ins) {
            Toast.makeText(HomeActivity.this, "instagram", Toast.LENGTH_LONG).show();
            GoToURL("https://www.instagram.com/");
        } else if (id == R.id.sp_fb) {
            Toast.makeText(HomeActivity.this, "facebook", Toast.LENGTH_LONG).show();
            GoToURL("https://www.facebook.com/botruongboyte.vn");
        } else if (id == R.id.sp_mail) {
            Toast.makeText(HomeActivity.this, "mail", Toast.LENGTH_LONG).show();
            Intent intent = new Intent(Intent.ACTION_VIEW);
            Uri data = Uri.parse("mailto:byt@moh.gov.vn");
            intent.setData(data);
            startActivity(intent);
        }
        return true;
    }

    //open mang xa hoi
    private void GoToURL(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

//onclick ->xxhdpi
    public void onClick(View v) {
        Intent intent = new Intent(HomeActivity.this, NewsActivity.class);
        startActivity(intent);
    }
}