package com.oldmonk.newsshots;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CategoryPageFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CategoryPageFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CategoryPageFragment extends android.support.v4.app.Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String POSITION = "position";
    private static final String LOCATION_ARGS = "location";

    public static final int LOCATION_INDIA = 0;
    public static final int LOCATION_USA = 1;
    // TODO: Rename and change types of parameters
    private String mCategory;
    private int mPosition;
    private String mLocation;


    //private OnFragmentInteractionListener mListener;
    RecyclerView rvNewsCards;
    ProgressBar progressBar;

    JSONArray articles;
    int numberOfArticles;

    public CategoryPageFragment() {
        // Required empty public constructor
    }

    public void setmPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    public void setmLocation(String mLocation) {
        this.mLocation = mLocation;
    }

    public String getmLocation() {
        return mLocation;
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param position Parameter 1.
     * @return A new instance of fragment CategoryPageFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CategoryPageFragment newInstance(int position,String locationPassed) {
        CategoryPageFragment fragment = new CategoryPageFragment();
        Bundle args = new Bundle();
        //args.putString(CATEGORY, category);
        ArrayList<String> locationList = new ArrayList<String>(2);
        locationList.add("in");
        locationList.add("us");
        args.putString(LOCATION_ARGS, locationPassed);
        args.putInt(POSITION, position);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mCategory = getArguments().getString(CATEGORY);
            mPosition = getArguments().getInt(POSITION);
            mLocation = getArguments().getString(LOCATION_ARGS);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View v = inflater.inflate(R.layout.fragment_category_page, container, false);
        progressBar = (ProgressBar)v.findViewById(R.id.progress_bar_fr);
        progressBar.setVisibility(View.VISIBLE);
        rvNewsCards = (RecyclerView)v.findViewById(R.id.rv_news_cards_fr);
        rvNewsCards.setVisibility(View.GONE);

        Context context = getContext();
        inflateRecyclerView(context, v);
        //TODO: take location input from somewhere, change in and put it in var



    return  v;
    }

    private void inflateRecyclerView(Context context, final View v){
        String COUNTRY = "country";
        String API_KEY = "apiKey";
        String MY_API_KEY = "075a6e95ccd443bab794e6a96e2ecfcf";
        String CATEGORY = "category";

        List<String> categoryList = new ArrayList<>(3);
        categoryList.add(" ");
        //TODO: make category 0
        categoryList.add("technology");
        categoryList.add("business");

        RequestQueue rq = Volley.newRequestQueue(context);
        List<String> urlList = new ArrayList<String>(2);
        String baseUri = "https://newsapi.org/v2/top-headlines";
        Uri requiredUri = Uri.parse(baseUri);
        if(mPosition>0){
        requiredUri = requiredUri.buildUpon()
                    .appendQueryParameter(COUNTRY, mLocation)
                    .appendQueryParameter(CATEGORY, categoryList.get(mPosition))
                    .appendQueryParameter(API_KEY, MY_API_KEY)
                    .build();
        }else{
        requiredUri= requiredUri.buildUpon()
                    .appendQueryParameter(COUNTRY, mLocation)
                    .appendQueryParameter(API_KEY, MY_API_KEY)
                    .build();
        }

        final String requiredUrlWithCountry = requiredUri.toString();

//        String urlTopNews = "https://newsapi.org/v2/top-headlines?country=in&apiKey=075a6e95ccd443bab794e6a96e2ecfcf";
//        String urlTech = "https://newsapi.org/v2/top-headlines?country=in&category=technology&apiKey=075a6e95ccd443bab794e6a96e2ecfcf";
//        String urlBus = "https://newsapi.org/v2/top-headlines?country=in&category=business&apiKey=075a6e95ccd443bab794e6a96e2ecfcf";
//        urlList.add(urlTopNews);
//        urlList.add(urlTech);
//        urlList.add(urlBus);
//
//        String url = urlList.get(mPosition);

        JsonObjectRequest jsonObjectRequest;
        jsonObjectRequest= new JsonObjectRequest(Request.Method.GET, requiredUrlWithCountry, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        numberOfArticles=0;
                        try {
                            numberOfArticles = response.getInt("totalResults");
                            Log.d(CategoryPageFragment.class.getName(), "onResponse: qwerty: "+ numberOfArticles);
                        } catch (JSONException e) {
                            Log.d(CategoryPageFragment.class.getName(), "onResponse: number not exist");
                        }
                        if(numberOfArticles>0){
                            try {
                                articles = response.getJSONArray("articles");
                                //JSONObject temp = article.getJSONObject(0);
                                //String titleRecieved = temp.getString("title");
                                //Log.d(LoggedIn.class.getName(), "onResponse: title received:"+ titleRecieved);
                                //Toast.makeText(LoggedIn.this, titleRecieved, Toast.LENGTH_SHORT).show();

                                progressBar.setVisibility(View.GONE);


                                rvNewsCards.setVisibility(View.VISIBLE);
                                rvNewsCards.setLayoutManager(new LinearLayoutManager(getContext()));
                                ArrayList<News> news = News.getNewsFromJsonArray(articles, numberOfArticles);
                                rvNewsCards.setAdapter(new AdapterNewsCard(news, getContext()));
                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.d(CategoryPageFragment.class.getName(), "onResponse: catch of articles");
                            }
                        }else {
                            Toast.makeText(getContext(), "No articles found", Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "An error occurred. Please try again", Toast.LENGTH_SHORT).show();
                Log.d(CategoryPageFragment.class.getName(), "url is "+requiredUrlWithCountry);
            }
        });
        rq.add(jsonObjectRequest);

    }

    // TODO: Rename method, update argument and hook method into UI event
    /*public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }*/

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    /*public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }*/
}
