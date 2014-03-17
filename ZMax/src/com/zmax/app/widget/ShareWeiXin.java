package com.zmax.app.widget;

import android.content.Context;
import android.graphics.BitmapFactory;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXTextObject;
import com.zmax.app.R;

/**
 * Created by ben on 14-2-11.
 * 分享微信
 */
public class ShareWeiXin {

//    private static final String WeiXinAppID = "wxda8a21cdffd0d0ab";//正式版
    private static final String WeiXinAppID = "wx6b4ebe1c2e117022";//测试版
    private IWXAPI api;
    private Context _context;

    public ShareWeiXin(Context context) {
        api = WXAPIFactory.createWXAPI(context, WeiXinAppID, true);
        api.registerApp(WeiXinAppID);
        _context = context;
    }

    /**
     * 分享给微信朋友
     *
     * @param content 分享内容
     */
    public void shareToFriend(String content) {
        WXTextObject text = new WXTextObject();
        text.text = content;

        WXMediaMessage message = new WXMediaMessage();
        message.mediaObject = text;
        message.description = content;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.scene = SendMessageToWX.Req.WXSceneSession;
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = message;
        api.sendReq(req);
    }


    /**
     * 分享给微信朋友
     *
     * @param content 分享内容
     */
    public void shareToFriendTimeline(String content) {
        WXTextObject text = new WXTextObject();
        text.text = content;

        WXMediaMessage message = new WXMediaMessage();
        message.mediaObject = text;
        message.description = content;
        message.setThumbImage(BitmapFactory.decodeResource(_context.getResources(), R.drawable.ic_launcher));
        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        req.transaction = String.valueOf(System.currentTimeMillis());
        req.message = message;
        api.sendReq(req);
    }

}
