package org.junglestory.openfire.opengraph.plugin;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OpenGraphParser {
	public OpenGraphParser() {
    }

    public OpenGraphTag parser(String url) {
    	OpenGraphTag tag = new OpenGraphTag();
        Map<String, List<String>> result = new HashMap<String,List<String>>();
        String[] metas = new String[]{"og:title", "og:type", "og:image", "og:url", "og:description" };

        try {
            Document doc = Jsoup
                    .connect(url)
                    .get();

            Elements elements = doc.select("meta[property^=og], meta[name^=og]");

            for (Element elm : elements) {
                String target= elm.hasAttr("property") ? "property" : "name";

                if(!result.containsKey(elm.attr(target))){
                    result.put(elm.attr(target), new ArrayList<String>());
                }

                result.get(elm.attr(target)).add(elm.attr("content"));
            }

            for(String meta : metas){
                if (!(result.containsKey(meta) && result.get(meta).size() > 0)){
                    if(meta.equals(metas[0])){
                        result.put(metas[0]
                                , Arrays.asList(new String[]{doc.select("title").eq(0).text()}));
                    } else if (meta.equals(metas[1])){
                        result.put(metas[1]
                                , Arrays.asList(new String[]{"website"}));
                    } else if (meta.equals(metas[2])){
                        result.put(metas[2]
                                , Arrays.asList(new String[]{doc.select("img").eq(0).attr("abs:src")}));
                    } else if (meta.equals(metas[3])){
                        result.put(metas[3]
                                , Arrays.asList(new String[]{doc.baseUri()}));
                    } else if (meta.equals(metas[4])){
                        result.put(metas[4]
                                , Arrays.asList(new String[]{doc.select("meta[property=description], meta[name=description]").eq(0).attr("content")}));
                    }
                }
            }

            for(String meta : result.keySet()) {
                if(meta.equals(metas[0])){
                    tag.setTitle(result.get(meta).get(0));
                } else if (meta.equals(metas[1])){
                    tag.setType(result.get(meta).get(0));
                } else if (meta.equals(metas[2])){
                    tag.setImage(result.get(meta).get(0));
                } else if (meta.equals(metas[3])){
                    tag.setUrl(result.get(meta).get(0));
                } else if (meta.equals(metas[4])){
                    tag.setDescription(result.get(meta).get(0));
                }
            }

            tag.setCreateDate(DateUtils.getCurrentDate());
        } catch (Exception e){
            e.printStackTrace();
        }

        return tag;
    }
}
