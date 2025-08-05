package dto;

import java.util.List;
import java.util.Map;

public class PageResult<T> {
    private List<T> data;
    private Map<String, Object> meta;

    public PageResult(List<T> data, long total, int page, int pageSize) {
        this.data = data;
        this.meta = Map.of(
                "count", data.size(),
                "page", page,
                "pageSize", pageSize,
                "totalPage", (total + pageSize - 1) / pageSize,
                "total", total
        );
    }

    public List<T> getData() { return data; }
    public Map<String, Object> getMeta() { return meta; }
}
