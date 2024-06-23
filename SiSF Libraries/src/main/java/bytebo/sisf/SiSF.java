package bytebo.sisf;
import java.util.*;
import java.util.regex.*;

public final class SiSF {
  private String data = "";
  private List<String> keys = new ArrayList();
  private List<String> vals = new ArrayList();
  
  public SiSF(String put){
    //Taking information
    data = put;
    int len = data.length();
    
    //Delete excessive spaces
    String exces = "";
    for(String i : data.split(" ")){
      if(i.length()>0){
        exces+=i+" ";
      }
    } data=exces.trim();
    
    //Delete notes (#! note !#)
    data=data.replaceAll("#\\!\\s*[^(\\!#)]*\\s*\\!#","");
    
    //Parsing to SiSF data
    String parsedata = data;
    while(parsedata.length()>0){
      Pattern pt4 = Pattern.compile("([a-zA-Z0-9_]+)\\s*\\{([^\\}]+)\\}");
      Matcher mt4 = pt4.matcher(parsedata);
      if(mt4.find()){
        String group = mt4.group(1);
        String child = mt4.group(2).trim();
        int count = 1;
        boolean isNull = false;
        if(child.equals("null")){
          isNull=true;
          keys.add("group::"+group);
          vals.add("[!type::null");
        }else{
          keys.add("group::"+group);
          vals.add("[!type::group");
        }
        while(child.length()>0&&!isNull){
            Pattern ptb = Pattern.compile("([a-zA-Z0-9_]+)\\s*:\\s*\"([^\"]*)\"");
            Matcher mtb = ptb.matcher(child);
            if(mtb.find()){
              String k = mtb.group(1);
              String v = mtb.group(2);
              keys.add("group::"+group+"::"+k);
              vals.add(v);
              child=child.replaceAll("^"+k+"\\s*:\\s*\""+v+"\"","").trim();
            }else{
              Pattern ptc = Pattern.compile("\"([^\"]*)\"");
              Matcher mtc = ptc.matcher(child);
              if(mtc.find()){
                String v = mtc.group(1);
                keys.add("group::"+group+"::#"+count);
                vals.add(v);
                count++;
                child=child.replaceAll("\""+v+"\"","").trim();
              }else{
                Pattern pth = Pattern.compile("([a-zA-Z0-9_]+)\\s*:\\s*null");
                Matcher mth = pth.matcher(child);
                if(mth.find()){
                  String k = mth.group(1);
                  keys.add("group::"+group+"::"+k);
                  vals.add("[!type::null");
                  child=child.replaceAll(k+"\\s*:\\s*null","").trim();
                }else{
                  break;
                }  
              }//else mtc
            }
        }//while
        parsedata=parsedata.replaceAll(group+"\\s*\\{\\s*[^\\}]+\\s*\\}","").trim();
       }else{
          Pattern pt = Pattern.compile("([a-zA-Z0-9_]+)\\s*:\\s*\"([^\"]*)\"");
          Matcher mt = pt.matcher(parsedata);
          if(mt.find()){
            String k = mt.group(1);
            String v = mt.group(2);
            keys.add(k);
            vals.add(v);
            parsedata=parsedata.replaceAll(k+"\\s*:\\s*\""+v+"\"","").trim();
          }else{
            Pattern pt2 = Pattern.compile("([a-zA-Z0-9_]+)\\s*:\\s*null");
            Matcher mt2 = pt2.matcher(parsedata);
            if(mt2.find()){
              String k = mt2.group(1);
              keys.add(k);
              vals.add("[!type::null");
              parsedata=parsedata.replaceAll(k+"\\s*:\\s*null","").trim();
            }else{
              break;
            }
        }
      }//else4  
    }
  }
  
  public String key(String x){
    int dx = 0;
    boolean found = false;
    for(String i : keys){
      if(i.equals(x.trim())){
        found=true;
        break;
      }else{
        dx++;
      }
    }
    if(found){
      if(vals.get(dx).trim().equals("[!type::null")){
        return "null";
      }else{
        return vals.get(dx).trim();
      }
    }else{
      return "unmatched";
    }
  }
  
  public String group(String x){
    try{
      String[] gp = x.split("/");
      String gpname = gp[0];
      String subkey = gp[1];
      int dx = 0;
      boolean found = false;
      for(String i : keys){
        if(i.equals("group::"+gpname+"::"+subkey)){
          found=true;
          break;
        }else{
          dx++;
        }
      }
      if(found){
        if(vals.get(dx).trim().equals("[!type::null")){
          return "null";
        }else{
          return vals.get(dx).trim();
        }
      }else{
        return "unmatched";
      }
    }catch(Exception e){
      try{
        int ds = 0;
        boolean yea = false;
        for(String i : keys){
          if(i.replace("group::","").equals(x)){
            yea=true;
            break;
          }else{
            ds++;
          }
        }
        if(yea){
          if(vals.get(ds).trim().equals("[!type::null")){
            return "null";
          }else{
            List<String> lst = new ArrayList();
            int indx = 0;
            for(String i : keys){
              Pattern pb = Pattern.compile("group::"+x+"::([a-zA-Z0-9_#]+)");
              Matcher mb = pb.matcher(i);
              if(mb.find()){
                String chkey = mb.group(1);
                if(vals.get(indx).equals("[!type::null")){
                  lst.add(chkey+":null");
                }else{
                  lst.add(chkey+":\""+vals.get(indx).trim()+"\"");
                }
              }
              indx++;
            }//for
            
            String texts = "<";
            int ln = 1;
            for(String i : lst){
              if(ln==lst.size()){
                texts+=i;
              }else{
                texts+=i+", ";
              }
              ln++;
            }
            String fx = texts+">";
            if(fx.equals("<>")&&fx.length()==2){
              return "unmatched";
            }else{
              return fx;
            }
          }//else
        }else{
          return "unmatched";
        }
      }catch(Exception f){
        return "unmatched";
      }
    }
  }
  
  public Map groupToMap(String i){
    String dat = group(i);
    Map<String,String> mp = new HashMap();
    if(dat.equals("unmatched")||dat.equals("null")){
      return mp;
    }else{
      while(true){
        Pattern pt = Pattern.compile("([a-zA-Z0-9_#]+):\"([^\"]*)\"");
        Matcher mt = pt.matcher(dat);
        if(mt.find()){
          String k = mt.group(1);
          String v = mt.group(2);
          mp.put(k,v);
          dat=dat.replace(k+":\""+v+"\"","");
        }else{
          Pattern pt2 = Pattern.compile("([a-zA-Z0-9_#]+):null");
          Matcher mt2 = pt2.matcher(dat);
          if(mt2.find()){
            String k = mt2.group(1);
            mp.put(k,"null");
            dat=dat.replace(k+":null","");
          }else{
            break;
          }
        }
      }//while
      return mp;
    }//else
  }
  
  public int lengthOfGroup(String ky){
    int len = 0;
    if(keys.contains("group::"+ky)){
      if(!vals.get(keys.indexOf("group::"+ky)).equals("[!type::null")){
        for(String i : keys){
          Pattern p = Pattern.compile("group::"+ky+"::[a-zA-Z0-9_#]+");
          Matcher m = p.matcher(i);
          if(m.find()){
            len++;
          }
        }
      }
    }
    return len;
  }
  
  public boolean isValued(String ky){
    String[] dat = ky.split("/");
    if(dat.length==1){
      if(keys.contains(ky)){
        int dx = keys.indexOf(ky);
        if(vals.get(dx).equals("[!type::null")){
          return false;
        }else{
          return true;
        }
      }else{
        return false;
      }
    }else{
      if(keys.contains("group::"+dat[0]+"::"+dat[1])){
        int dx = keys.indexOf("group::"+dat[0]+"::"+dat[1]);
        if(vals.get(dx).equals("[!type::null")){
          return false;
        }else{
          return true;
        }
      }else{
        return false;
      }
    }
  }
  
  public void toNull(String key){
    String[] dat = key.split("/");
    int inxt = 0;
    if(dat.length==1){
      int dx = 0;
      boolean found = false;
      for(String i : keys){
        if(i.contains(key)){
          found = true;
          break;
        }else{
          dx++;
        }
      }
      
      if(found){
        Pattern pt = Pattern.compile("group::"+key);
        Matcher mt = pt.matcher(keys.get(dx));
        if(mt.find()){
          vals.set(dx,"[!type::null");
          List<String> ks = new ArrayList(keys);
          List<String> vs = new ArrayList(vals);
          for(String i : keys){
            if(i.contains("group::"+key+"::")){
              int inx = ks.indexOf(i);
              ks.remove(inx);
              vs.remove(inx);
            }
          }
          keys=ks;
          vals=vs;
        }else{
          for(String i:keys){
            Pattern px = Pattern.compile("([a-zA-Z0-9_]+)");
            Matcher mx = px.matcher(i);
            if(mx.find()){
              if(mx.group(1).equals(key)){
                vals.set(keys.indexOf(key),"[!type::null");
              }
            }
          }
        }
      }
    }else{
      if(keys.contains("group::"+dat[0]+"::"+dat[1])){
        vals.set(keys.indexOf("group::"+dat[0]+"::"+dat[1]),"[!type::null");
      }
    }
  }
  
  public String getSource() {
    List<String> k = new ArrayList(keys);
    List<String> v = new ArrayList(vals);
    int cn = 0;
    String struct = "";
    
    //Print key (non-group)
    for(String i : k){
      if(!i.contains("group::")){
        if(v.get(cn).equals("[!type::null")){
          struct+="\n"+k.get(cn)+": null";
        }else{
          struct+="\n"+k.get(cn)+": \""+v.get(cn)+"\"";
        }
        k.set(cn,"##deleted!");
        v.set(cn,"##deleted!");
      }cn++;
    }cn=0;
    
    //Print group key
    for(String i : k){
      Pattern pt = Pattern.compile("group::[a-zA-Z0-9_]+");
      Matcher mt = pt.matcher(i);
      if(mt.matches()){
        struct+="\n"+k.get(cn).replaceAll("^group::","")+" {";
        String vs = v.get(cn);
        String group = k.get(cn);
        v.set(cn,"##deleted!"); k.set(cn,"##deleted!");
        if(vs.equals("[!type::null")){
          struct+="\n  null";
        }else{
          int ox = 0;
          for(String j:k){
            Pattern px = Pattern.compile(group+"::([a-zA-Z0-9_#]+)");
            Matcher mx = px.matcher(j);
            if(mx.find()){
              if(mx.group(1).contains("#")){
                if(v.get(ox).equals("[!type::null")){
                  struct+="\n  null";
                }else{
                  struct+="\n  \""+v.get(ox)+"\"";
                }
                k.set(ox,"##deleted!");
              }else{
                if(v.get(ox).equals("[!type::null")){
                  struct+="\n  "+k.get(ox).replace(group+"::","")+": null";
                }else{
                  struct+="\n  "+k.get(ox).replace(group+"::","")+": \""+v.get(ox)+"\"";
                }
                k.set(ox,"##deleted!");
              }
            }ox++;
          }
        }
        struct+="\n}";
      }cn++;
    }
    return struct.trim();
  }
  
}


/*
Methods:
1. key
2. group
3. groupToMap
4. lengthOfGroup
5. isValued
6. toNull
7. getSource
*/

