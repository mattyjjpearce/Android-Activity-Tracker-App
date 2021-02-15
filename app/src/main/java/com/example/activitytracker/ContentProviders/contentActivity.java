package com.example.activitytracker.ContentProviders;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.database.Cursor;
import android.os.Bundle;

import com.example.activitytracker.R;

public class contentActivity extends AppCompatActivity {

    private static final int LOADER = 1;

    private ProviderAdapter adapter;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_content);

        final RecyclerView list = findViewById(R.id.contentRecycler);
        list.setLayoutManager(new LinearLayoutManager(list.getContext()));
        adapter = new ProviderAdapter();
        list.setAdapter(adapter);

        LoaderManager.getInstance(this).initLoader(LOADER, null, mLoaderCallbacks);

    }

    private final LoaderManager.LoaderCallbacks<Cursor> mLoaderCallbacks =
            new LoaderManager.LoaderCallbacks<Cursor>() {

                @Override
                @NonNull
                public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
                    return new CursorLoader(getApplicationContext(),
                            ActivityProvider.URI_ITEM,
                            new String[]{"title"},
                            null, null, null);
                }
                @Override
                public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
                    adapter.setActivity(data);
                }

                @Override
                public void onLoaderReset(@NonNull Loader<Cursor> loader) {
                    adapter.setActivity(null);
                }

            };
}