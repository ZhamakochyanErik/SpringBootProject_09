package friendfinder.net.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

@Component
public class PaginationUtil {

    public int getPaginationLength(int count,int size){
        int length;
        if(count < size){
            length = 1;
        }else if(count % size == 0){
            length = count/size;
        }else {
            length = count/size + 1;
        }
        return length;
    }

    public Pageable checkPageable(Pageable pageable,int length){
        if(pageable.getPageNumber() >= length){
            return PageRequest.of(0,pageable.getPageSize());
        }
        return pageable;
    }
}