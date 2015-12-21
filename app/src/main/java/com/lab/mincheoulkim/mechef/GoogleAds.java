package com.lab.mincheoulkim.mechef;

import android.content.Context;

import com.google.ads.conversiontracking.AdWordsConversionReporter;
import com.google.ads.conversiontracking.AdWordsRemarketingReporter;

import java.util.HashMap;
import java.util.Map;

/*
   Deinfe app remarketing parameters

   page_type : activity_main, activity_detail
   action_type : activity_view, select_dish, add_comment, stop_comment, no_comment

   Conversion Name : MeChef Detail Page View
   Conversion ID: 904594152
   Conversion label: fxSiCOvHrmEQ6IWsrwM

    Conversion Name : MeChef Detail Page Add Comment
    Conversion ID: 904594152
    Conversion label: 7f_JCL7ErmEQ6IWsrwM

    Google Analytics
    Me Chef Android App
    Property ID : UA-63185670-5
 */


/**
 * Created by mincheoulkim on 11/15/15.
 */
public class GoogleAds {
    String sConversionId = "904594152"; // mincheoukim+aw@google.com account

    public boolean ConversionReport (Context applicationContext, String sConversionLabel) {
        if(applicationContext == null || sConversionLabel.isEmpty())
            return false;

        AdWordsConversionReporter.reportWithConversionId(applicationContext, sConversionId,sConversionLabel,"0",true);
        return true;
    }

    public boolean RemarketingReport(Context applicationContext, String sPageType, String sActionType) {
        if(sPageType.isEmpty() || sActionType.isEmpty())
            return false;

        Map<String, Object> params = new HashMap<String, Object>();
        params.put("page_type", sPageType);
        params.put("action_type", sActionType);

        AdWordsRemarketingReporter.reportWithConversionId( applicationContext, sConversionId, params);
        return true;
    }
}
