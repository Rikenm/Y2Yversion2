package com.rikenmaharjan.y2yc.fragments;

/**
 * Created by Riken on 5/28/18.
 */

import android.app.Fragment;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.rikenmaharjan.y2yc.R;
import com.rikenmaharjan.y2yc.activities.Main2Activity;
import com.rikenmaharjan.y2yc.utils.SessionManager;
import com.shockwave.pdfium.PdfDocument;

import java.net.URI;
import java.util.List;

public class HandBookFragment extends Fragment implements OnPageChangeListener,OnLoadCompleteListener{
    private static final String TAG = Main2Activity.class.getSimpleName();
    public static final String SAMPLE_FILE = "Guest-Handbook.pdf";
    PDFView pdfView;
    Integer pageNumber = 0;
    String pdfFileName;
    public SessionManager session;


    @Override
    public void onResume() {
        super.onResume();


        session = new SessionManager(getActivity());

        session.checkLogin();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_handbook, container, false);

        // Get ref of pdfView in layout
        pdfView = view.findViewById(R.id.pdfView);

        pdfView= (PDFView) view.findViewById(R.id.pdfView);
        displayFromAsset(SAMPLE_FILE);
        return view;
    }

    private void displayFromAsset(String assetFileName) {
        pdfFileName = assetFileName;

        //pdfView.fromAsset(SAMPLE_FILE)
        pdfView.fromUri(Uri.parse("https://s3.us-east-2.amazonaws.com/y2ydemo1/Guest-Handbook.pdf"))
                .defaultPage(pageNumber)
                .enableSwipe(true)

                .swipeHorizontal(false)
                .onPageChange(this)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(getActivity()))
                .load();
    }


    @Override
    public void onPageChanged(int page, int pageCount) {
        pageNumber = page;
        //setTitle(String.format("%s %s / %s", pdfFileName, page + 1, pageCount));
    }

    //TODO: add progress bar in the layout
    @Override
    public void loadComplete(int nbPages) {


        PdfDocument.Meta meta = pdfView.getDocumentMeta();
        printBookmarksTree(pdfView.getTableOfContents(), "-");

    }

    public void printBookmarksTree(List<PdfDocument.Bookmark> tree, String sep) {
        for (PdfDocument.Bookmark b : tree) {

            Log.e(TAG, String.format("%s %s, p %d", sep, b.getTitle(), b.getPageIdx()));

            if (b.hasChildren()) {
                printBookmarksTree(b.getChildren(), sep + "-");
            }
        }
    }

}