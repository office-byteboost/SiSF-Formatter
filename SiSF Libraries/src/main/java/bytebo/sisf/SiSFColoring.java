package bytebo.sisf;

import android.widget.*;
import android.text.*;
import android.text.style.*;
import android.app.Activity;
import android.os.Bundle;
import java.util.*;
import java.util.regex.*;
import android.graphics.Color;

public final class SiSFColoring {
  private String cnull_li = "f70063";
  private String cnull_da = "f70090";
  private String ccolon_li = "003ca3";
  private String ccolon_da = "6695ff";
  private String cbracket_li = "5000d2";
  private String cbracket_da = "b7a6d2";
  private String ckey_li = "005bf7";
  private String ckey_da = "009cf7";
  private String cvalue_li = "f77300";
  private String cvalue_da = "f7da00";
  private String cunkeyed_li = "04bd60";
  private String cunkeyed_da = "00f75f";
  private String cnotes_li = "c1c1c1";
  private String cnotes_da = "4A5D87";
  private String cquote_li = "f75200";
  private String cquote_da = "f7b500";
  private String cquoteUnkeyed_li = "038745";
  private String cquoteUnkeyed_da = "44ea94";
  private int md = 0;

  SiSFColoring(final EditText ed, Mode mode, Activity act) {
    md = 0;
    switch(mode){
      case LIGHT : md=0; break;
      case DARK : md=1;
    }
    act.runOnUiThread(
      new Runnable() {
        @Override
        public void run() {
          initialize(ed);
        }
    });
  }

  private void initialize(final EditText ed) {
    ed.addTextChangedListener(
      new TextWatcher() {
        @Override
        public void onTextChanged(CharSequence _param1, int _param2, int _param3, int _param4) {
          String chq = _param1.toString();
          SpannableString span = new SpannableString(chq);
          int cnull = 0;
          int ccolon = 0;
          int cbracket = 0;
          int ckey = 0;
          int cvalue = 0;
          int cunkeyed = 0;
          int cnotes = 0;
          int cquote = 0;
          int cquoteUnkeyed = 0;
          if(md==0){
            cnull = Color.parseColor("#"+cnull_li);
            ccolon = Color.parseColor("#"+ccolon_li);
            cbracket = Color.parseColor("#"+cbracket_li);
            ckey = Color.parseColor("#"+ckey_li);
            cvalue = Color.parseColor("#"+cvalue_li);
            cunkeyed = Color.parseColor("#"+cunkeyed_li);
            cnotes = Color.parseColor("#"+cnotes_li);
            cquote = Color.parseColor("#"+cquote_li);
            cquoteUnkeyed = Color.parseColor("#"+cquoteUnkeyed_li);
          }else{
            cnull = Color.parseColor("#"+cnull_da);
            ccolon = Color.parseColor("#"+ccolon_da);
            cbracket = Color.parseColor("#"+cbracket_da);
            ckey = Color.parseColor("#"+ckey_da);
            cvalue = Color.parseColor("#"+cvalue_da);
            cunkeyed = Color.parseColor("#"+cunkeyed_da);
            cnotes = Color.parseColor("#"+cnotes_da);
            cquote = Color.parseColor("#"+cquote_da);
            cquoteUnkeyed = Color.parseColor("#"+cquoteUnkeyed_li);
          }
            
            // {
            Pattern p1 = Pattern.compile("\\{");
            Matcher m1 = p1.matcher(chq);
            // }
            Pattern p2 = Pattern.compile("\\}");
            Matcher m2 = p2.matcher(chq);
            // null
            Pattern p3 = Pattern.compile("\\bnull\\b");
            Matcher m3 = p3.matcher(chq);
            // group key
            Pattern p8 = Pattern.compile("([a-zA-Z0-9_]+)\\s*\\{[^\\}]+\\}");
            Matcher m8 = p8.matcher(chq);
            // unkeyed (c: quoteUnkeyed, unkeyed)
            Pattern p4 = Pattern.compile("(\")([^\"]*)(\")");
            Matcher m4 = p4.matcher(chq);
            // key:value (c: key, colon, quote, value)
            Pattern p5 = Pattern.compile("([a-zA-Z0-9_]+)(\\s*)(:)(\\s*)(\")([^\"]*)(\")");
            Matcher m5 = p5.matcher(chq);
            // key:null (c: key, colon)
            Pattern p6 = Pattern.compile("([a-zA-Z0-9_]+)(\\s*)(:)(\\s*)null");
            Matcher m6 = p6.matcher(chq);
            // note
            Pattern p7 = Pattern.compile("(#\\![^(\\!#)]*\\!#)");
            Matcher m7 = p7.matcher(chq);
            
            // {
            while(m1.find()){
            ForegroundColorSpan bracket = new ForegroundColorSpan(cbracket);
              span.setSpan(bracket,m1.start(),m1.end(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            // }
            while(m2.find()){
            ForegroundColorSpan bracket = new ForegroundColorSpan(cbracket);
              span.setSpan(bracket,m2.start(),m2.end(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            // null
            while(m3.find()){
            ForegroundColorSpan nul = new ForegroundColorSpan(cnull);
              span.setSpan(nul,m3.start(),m3.end(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            //group key
            while(m8.find()){
            ForegroundColorSpan key = new ForegroundColorSpan(ckey);
              span.setSpan(key,m8.start(1),m8.end(1),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            // unkeyed
            while(m4.find()){
            ForegroundColorSpan quoteUnkeyed = new ForegroundColorSpan(cquoteUnkeyed);
            ForegroundColorSpan unkeyed = new ForegroundColorSpan(cunkeyed);
              span.setSpan(quoteUnkeyed,m4.start(1),m4.end(1),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
              span.setSpan(unkeyed,m4.start(2),m4.end(2),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
              ForegroundColorSpan quoteUnkeyed2 = new ForegroundColorSpan(cquoteUnkeyed);
              span.setSpan(quoteUnkeyed2,m4.start(3),m4.end(3),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            // key:value
            while(m5.find()){
            ForegroundColorSpan key = new ForegroundColorSpan(ckey);
              span.setSpan(key,m5.start(1),m5.end(1),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
              ForegroundColorSpan colon = new ForegroundColorSpan(ccolon);
              span.setSpan(colon,m5.start(3),m5.end(3),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
              ForegroundColorSpan quote = new ForegroundColorSpan(cquote);
              span.setSpan(quote,m5.start(5),m5.end(5),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
              ForegroundColorSpan value = new ForegroundColorSpan(cvalue);
              span.setSpan(value,m5.start(6),m5.end(6),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
              ForegroundColorSpan quote2 = new ForegroundColorSpan(cquote);
              span.setSpan(quote2,m5.start(7),m5.end(7),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            // key:value
            while(m6.find()){
              ForegroundColorSpan key = new ForegroundColorSpan(ckey);
              span.setSpan(key,m6.start(1),m6.end(1),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
              ForegroundColorSpan colon = new ForegroundColorSpan(ccolon);
              span.setSpan(colon,m6.start(3),m6.end(3),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            // note
            while(m7.find()){
              ForegroundColorSpan notes = new ForegroundColorSpan(cnotes);
              span.setSpan(notes,m7.start(),m7.end(),Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            }
            
          ed.removeTextChangedListener(this);
          int pos = ed.getSelectionStart();
          Editable editable = ed.getText();
          editable.replace(0, editable.length(), span);
          ed.setSelection(pos);
          ed.addTextChangedListener(this);
        }

        @Override
        public void beforeTextChanged(
          CharSequence _param1, int _param2, int _param3, int _param4) {}

        @Override
          public void afterTextChanged(Editable _param1) {}
    });
  }
  
  public void modify(String cl, Target tg, Mode mode){
    Pattern pt = Pattern.compile("#([^#]+)");
    Matcher mt = pt.matcher(cl);
    if(mt.find()){
      cl=mt.group(1);
    }
    switch(mode){
      case LIGHT:
         switch(tg){
           case NULL: cnull_li=cl; break;
           case COLON: ccolon_li=cl; break;
           case BRACKET: cbracket_li=cl; break;
           case KEY: ckey_li=cl; break;
           case VALUE: cvalue_li=cl; break;
           case UNKEYED_VALUE: cunkeyed_li=cl; break;
           case NOTES: cnotes_li=cl; break;
           case QUOTE: cquote_li=cl; break;
           case QUOTE_UNKEYED: cquoteUnkeyed_li=cl; break;
         }break;
      case DARK:
         switch(tg){
           case NULL: cnull_da=cl; break;
           case COLON: ccolon_da=cl; break;
           case BRACKET: cbracket_da=cl; break;
           case KEY: ckey_da=cl; break;
           case VALUE: cvalue_da=cl; break;
           case UNKEYED_VALUE: cunkeyed_da=cl; break;
           case NOTES: cnotes_da=cl; break;
           case QUOTE: cquote_da=cl; break;
           case QUOTE_UNKEYED: cquoteUnkeyed_da=cl; break;
         }break;
    }
  }
  
  public void reset(){
    cnull_li = "f70063";
    cnull_da = "f70090";
    ccolon_li = "003ca3";
    ccolon_da = "6695ff";
    cbracket_li = "5000d2";
    cbracket_da = "b7a6d2";
    ckey_li = "005bf7";
    ckey_da = "009cf7";
    cvalue_li = "f77300";
    cvalue_da = "f7da00";
    cunkeyed_li = "04bd60";
    cunkeyed_da = "00f75f";
    cnotes_li = "c1c1c1";
    cnotes_da = "4A5D87";
    cquote_li = "f75200";
    cquote_da = "f7b500";
    cquoteUnkeyed_li = "038745";
    cquoteUnkeyed_da = "44ea94";
  }
}

enum Mode {
  LIGHT, DARK
}

enum Target {
  NULL, COLON, BRACKET, KEY, VALUE, UNKEYED_VALUE, NOTES, QUOTE, QUOTE_UNKEYED
}



/*
Methods:
1. replaceColor
2. resetColor
*/


