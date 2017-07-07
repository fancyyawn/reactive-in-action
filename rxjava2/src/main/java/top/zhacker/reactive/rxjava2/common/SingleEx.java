package top.zhacker.reactive.rxjava2.common;

import java.util.function.Function;

import io.reactivex.Single;


/**
 * Created by zhacker.
 * Time 2017/6/25 下午4:15
 * Desc 文件描述
 */
public final class SingleEx {
    
    public static <T> Single<T> fromMethod(Function<Long, T> finder, Long id){
        
        Single<T> findSingle = Single.create(emitter -> {
            T result = finder.apply(id);
            if(result == null){
                emitter.onError(new RuntimeException("not.exist"));
            }else{
                emitter.onSuccess(result);
            }
        });
        return Single.defer(()-> findSingle);
    }
}
