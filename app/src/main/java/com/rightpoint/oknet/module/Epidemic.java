package com.rightpoint.oknet.module;

import java.util.List;

/**
 * Description：
 * @author Wonder Wei
 * Create date：2020/9/22 5:37 PM 
 */
public class Epidemic {

    public int total;
    public List<InformationBean> list;

    public static class InformationBean {
        public int clickCount;
        public String content;
        public int id;
        public String imgOneUrl;
        public Object imgThreeUrl;
        public Object imgTwoUrl;
        public int imgViewType;
        public String infoSourceText;
        public String infoStatement;
        public String infoTitle;
        public int isNew;
        public long publicTime;
        public int realClickCount;
    }
}
