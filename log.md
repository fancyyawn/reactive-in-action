http://www.jianshu.com/p/464fa025229e 

Observable -> Observer/Consumer/Action: subscribe、dispose
ObservableEmitter： onNext(T value)、onComplete()和onError(Throwable error)
Observer: onNext onError onComplete onSubscribe
Disposable: dispose

public final Disposable subscribe() {}
public final Disposable subscribe(Consumer<? super T> onNext) {}
public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError) {} 
public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete) {}
public final Disposable subscribe(Consumer<? super T> onNext, Consumer<? super Throwable> onError, Action onComplete, Consumer<? super Disposable> onSubscribe) {}
public final void subscribe(Observer<? super T> observer) {}
    
subscribeOn(Schedulers.newThread()): 指定上游发送事件的线程，多次指定则第一次生效。                          
observeOn(AndroidSchedulers.mainThread())：指定的是下游接收事件的线程， 多次会以区间的形式生效。
doOnNext/Error/Complete/Subscribe/Dispose/Each/Lifecycle(Subscribe/Dispose)/Terminate(Error/Complete)：打日志，更新信息/

Schedulers.io() 代表io操作的线程, 通常用于网络,读写文件等io密集型的操作
Schedulers.computation() 代表CPU计算密集型的操作, 例如需要大量计算的操作
Schedulers.newThread() 代表一个常规的新线程

