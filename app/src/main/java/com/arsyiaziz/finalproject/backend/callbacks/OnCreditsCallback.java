package com.arsyiaziz.finalproject.backend.callbacks;

import com.arsyiaziz.finalproject.backend.models.CreditModel;

import java.util.List;

public interface OnCreditsCallback {
    void onSuccess(List<CreditModel> creditsList, String msg);
    void onFailure(String msg);
}
