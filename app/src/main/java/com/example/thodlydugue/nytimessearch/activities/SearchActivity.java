package com.example.thodlydugue.nytimessearch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.Toast;

import com.example.thodlydugue.nytimessearch.Article;
import com.example.thodlydugue.nytimessearch.ArticleArrayAdapter;
import com.example.thodlydugue.nytimessearch.R;
import com.example.thodlydugue.nytimessearch.activities.ArticleActivity;
import com.example.thodlydugue.nytimessearch.activities.SettingsActivity;
import com.example.thodlydugue.nytimessearch.models.SearchSettings;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

import static com.example.thodlydugue.nytimessearch.R.string.search;


public class SearchActivity extends AppCompatActivity {

    GridView gvResults;


    ArrayList<Article> articles;
    ArticleArrayAdapter adapter;
    SearchSettings settings;
    String searchQuery;
    int searchPage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setupSearchParameters();
        setupViews();

    }

    public void setupSearchParameters()
    {
        settings = new SearchSettings();
    }

    public void setupViews() {
        gvResults = (GridView) findViewById(R.id.gvResults);
        articles = new ArrayList<Article>();
        adapter = new ArticleArrayAdapter(this, articles);
        gvResults.setAdapter(adapter);
        searchPage = 0;
        gvResults.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                Article article = (Article)adapterView.getItemAtPosition(position);
                Intent i = new Intent(getApplicationContext(), ArticleActivity.class);
                i.putExtra("article", article);
                startActivity(i);

            }
        });

        gvResults.setOnScrollListener(new EndlessScrollListener(5, 0) {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {
                // Triggered only when new data needs to be appended to the list
                // Add whatever code is needed to append new items to your AdapterView

                searchPage = page;
                Toast.makeText(SearchActivity.this, "LoadMore "+searchPage+" - "+totalItemsCount, Toast.LENGTH_LONG).show();

                return true;
            }
        });
    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_search, menu);
        MenuItem searchItem = menu.findItem(R.id.action_search);
        final SearchView searchView = (SearchView) MenuItemCompat.getActionView(searchItem);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchQuery = query;
                searchArticles();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            showSettings();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }



    public void searchArticles() {
        AsyncHttpClient client = new AsyncHttpClient();
        String url = "http://api.nytimes.com/svc/search/v2/articlesearch.json";
        RequestParams params = new RequestParams();

        params.put("api-key", "d4c51048bf77499f95e366522fb91bfa");
        params.put("page", searchPage);
        params.put("q", searchQuery);

        if(settings.getBeginDate() != null && settings.getBeginDate().getCalendar() != null) {
            params.put("begin_date", settings.formatBeginDate());
        }

        if(settings.getSortOrder() != SearchSettings.Sort.none) {
            params.put("sort", settings.getSortOrder().name());
        }
        if(settings.getFilters().size() > 0) {
            params.put("fq", settings.generateNewsDeskFiltersOR());
        }

        client.get(url, params, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                Log.d("DEBUG", response.toString());
                JSONArray articleJsonResults = null;

                try {
                    articleJsonResults = response.getJSONObject("response").getJSONArray("docs");
                    if (searchPage == 0)
                        adapter.clear();
                    adapter.addAll(Article.fromJSONArray(articleJsonResults));
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        });
    }
    public void showSettings() {
        //Toast.makeText(SearchActivity.this, "Settings Dialog Fragment will open", Toast.LENGTH_SHORT).show();
        Intent i = new Intent(this, SettingsActivity.class);
        i.putExtra("settings", settings);
        startActivityForResult(i, 100);

    }

    // Handle the result once the activity returns a result
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == 100) {
            if(resultCode == RESULT_OK) {
                settings = (SearchSettings) data.getSerializableExtra("settings");
                //Toast.makeText(this, "sort passed: "+ settings.getSortOrder(), Toast.LENGTH_SHORT).show();
            }
        }
    }

}
