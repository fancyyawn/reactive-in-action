# reactive stream

## 参考
* [给初学者的RxJava2.0教程](http://www.jianshu.com/u/c50b715ccaeb)
* [rxjava wiki](https://github.com/ReactiveX/RxJava/wiki)
* [rxjava code](https://github.com/ReactiveX/RxJava)
* [reactivex](http://reactivex.io/documentation/operators.html)
* [Reactive Programming with RxJava: Creating Asynchronous, Event-Based Applications](https://www.amazon.com/Reactive-Programming-RxJava-Asynchronous-Applications/dp/1491931655/ref=sr_1_1)
* [reactive stream](https://github.com/reactive-streams/reactive-streams-jvm)

## 相关名词

* reactiveStream
* Akka
* reactor（pivotal，Spring）
* rxJava2
* rxAndroid
* rxJs
* rxNetty

## rxjava2可以干什么？

### push vs pull
```java

interface Iterator<T>{
    boolean hasNext();
    E next();
}

public interface Emitter<T> {
    void onNext(@NonNull T value); //
    void onError(@NonNull Throwable error);
    void onComplete();
}

public interface FlowableEmitter<T> extends Emitter<T> {

    long requested();
    boolean isCancelled();
    void setDisposable(@Nullable Disposable s);
    void setCancellable(@Nullable Cancellable c);
}

public interface Disposable {
    void dispose(); //连接是关闭的
    boolean isDisposed();
}

public interface Subscriber<T> {

    void onSubscribe(Subscription s);
    void onNext(T t);
    void onError(Throwable t);
    void onComplete();
}
public interface Subscription {
    void request(long n);
    void cancel();
}

public interface ObservableEmitter<T> extends Emitter<T> {
    
    void setDisposable(@Nullable Disposable d);
    void setCancellable(@Nullable Cancellable c);
    boolean isDisposed();
}
public interface Observer<T> {

    void onSubscribe(@NonNull Disposable d);
    void onNext(@NonNull T t);
    void onError(@NonNull Throwable e);
    void onComplete();
}

```

### Async versus Sync
```java
public class SyncOrAsync{
    
    public Observable<User> findUserByTokenSync(String token, Function<String, User> finder){
     return Observable.fromCallable(()-> findUserByToken(token, finder));
    }
    public Observable<User> findUserByTokenAsync(String token, Function<String, User> finder){
     return Observable.fromCallable(()-> findUserByToken(token, finder)).subscribeOn(Schedulers.io());
    }   
}
```

### Lazy versus Eager
```java
public class Lazy{
    
    public Observable<User> list(){
        return Observable.defer(
                ()-> Observable.fromIterable(userRepo.findAll())
        );
    }
    
    public void test() throws Exception{
        //创建一个上游 Observable：
        Observable<Integer> observable =  Observable.create(emitter -> {
            emitter.onNext(1);
            emitter.onNext(2);
            emitter.onNext(3);
            emitter.onComplete();
        });
        //创建一个下游 Observer
        Observer<Integer> observer = new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                log.info("subscribe");
            }
        
            @Override
            public void onNext(Integer value) {
                log.info("" + value);
            }
        
            @Override
            public void onError(Throwable e) {
                log.info("error");
            }
        
            @Override
            public void onComplete() {
                log.info("complete");
            }
        };
        //建立连接, 在连接之前，Observable不会吐数据。并且可以多次subscribe。
        //从而Observable和Observer可以分开建模，并重复使用。
        observable.subscribe(observer);
    }
}
```

### 任务编排
````java
public class TaskSchedule{
    
    public Observable<User> loginParallel(String token, String password){
        return Observable.merge( //同时发出多个异步任务
                    findUserByTokenAsync(token, userRepo::findByName),
                    findUserByTokenAsync(token, userRepo::findByMobile),
                    findUserByTokenAsync(token, userRepo::findByEmail))
                .filter(user -> user.getPassword().equals(password))
                .doOnNext(user -> user.setPassword(null));
    }
    
    public Observable<User> loginSequential(String token, String password){
        return Observable.concat( //逐个发出异步任务
                    findUserByTokenAsync(token, userRepo::findByName),
                    findUserByTokenAsync(token, userRepo::findByMobile),
                    findUserByTokenAsync(token, userRepo::findByEmail))
                .filter(user -> user.getPassword().equals(password) )
                .doOnNext(user -> user.setPassword(null));
    }
    
   public Observable<User> register(User user){
        return Observable.zip( //同时发起多个校验， 每个校验异步执行
                checkAsync(user, this::checkName),
                checkAsync(user, this::checkMobile),
                checkAsync(user, this::checkEmail),
                (a, b, c)-> userRepo.save(user)
        );
    }
}
````
### 限流
```java
Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; ; i++) {
                    emitter.onNext(i);
                }
            }
        }).subscribeOn(Schedulers.io())
                .filter(new Predicate<Integer>() { 
                    @Override
                    public boolean test(Integer integer) throws Exception {
                        return integer % 10 == 0; //数据特征过滤
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "" + integer);
                    }
                });

Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; ; i++) {
                    emitter.onNext(i);
                }
            }
        }).subscribeOn(Schedulers.io())
                .sample(2, TimeUnit.SECONDS)  //sample取样
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "" + integer);
                    }
                });

Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; ; i++) {
                    emitter.onNext(i);
                    Thread.sleep(2000);  //每次发送完事件延时2秒
                }
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Consumer<Integer>() {
                    @Override
                    public void accept(Integer integer) throws Exception {
                        Log.d(TAG, "" + integer);
                    }
                });

```

### 背压，控制流速

请参考 top.zhacker.reactive.rxjava2.FileDisplayer
```java
Flowable<Integer> upstream = Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                Log.d(TAG, "emit 1");
                emitter.onNext(1);
                Log.d(TAG, "emit 2");
                emitter.onNext(2);
                Log.d(TAG, "emit 3");
                emitter.onNext(3);
                Log.d(TAG, "emit complete");
                emitter.onComplete();
            }
        }, BackpressureStrategy.ERROR); //增加了一个参数

        Subscriber<Integer> downstream = new Subscriber<Integer>() {

            @Override
            public void onSubscribe(Subscription s) {
                Log.d(TAG, "onSubscribe");
                s.request(Long.MAX_VALUE);  //注意这句代码
            }

            @Override
            public void onNext(Integer integer) {
                Log.d(TAG, "onNext: " + integer);

            }

            @Override
            public void onError(Throwable t) {
                 Log.w(TAG, "onError: ", t);
            }

            @Override
            public void onComplete() {
                Log.d(TAG, "onComplete");
            }
        };

        upstream.subscribe(downstream);
        
```

```java
Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; ; i++) {
                    emitter.onNext(i);
                }
            }
        }, BackpressureStrategy.BUFFER).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {

                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.d(TAG, "onSubscribe");
                        mSubscription = s;
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.w(TAG, "onError: ", t);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });
```

```java
Flowable.create(new FlowableOnSubscribe<Integer>() {
            @Override
            public void subscribe(FlowableEmitter<Integer> emitter) throws Exception {
                for (int i = 0; i < 10000; i++) {  //只发1w个事件
                    emitter.onNext(i);
                }
            }
        }, BackpressureStrategy.DROP/LATEST).subscribeOn(Schedulers.io()) //注册策略
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Integer>() {

                    @Override
                    public void onSubscribe(Subscription s) {
                        Log.d(TAG, "onSubscribe");
                        mSubscription = s;
                        s.request(128);  //一开始就处理掉128个事件
                    }

                    @Override
                    public void onNext(Integer integer) {
                        Log.d(TAG, "onNext: " + integer);
                    }

                    @Override
                    public void onError(Throwable t) {
                        Log.w(TAG, "onError: ", t);
                    }

                    @Override
                    public void onComplete() {
                        Log.d(TAG, "onComplete");
                    }
                });
```

### 与高性能Web容器结合使用(Netty等)

```java
public class ReactiveSpringMvc{
    
    @RequestMapping(method = RequestMethod.GET, value = "/multiple")
    public Single<List<String>> multiple() {
        return Observable.just("multiple", "values").toList().toSingle();
    }
}
```

### 与MQ结合使用
```java
@EnableBinding(Source.class)
@Slf4j
public class TimerSource {

  @Bean
  @InboundChannelAdapter(value = Source.OUTPUT, poller = @Poller(fixedDelay = "${fixedDelay}", maxMessagesPerPoll = "1"))
  public MessageSource<String> timerMessageSource() {
    return () -> {
      return new GenericMessage<>("hello at " + LocalDateTime.now().toString());
    };
  }
}
@EnableBinding(Processor.class)
@Slf4j
public class MessageConsumer {

    @StreamListener(target = Processor.OUTPUT)
    public void receiveMsg(Observable<String> msg) {
        msg.subscribe(x-> log.info(x));
    }
}
```

### 与EventSource配合使用
```java
public class ReactiveEventSource{
    
    @RequestMapping(method = RequestMethod.GET, value = "/messagesInterval")
    public SseEmitter messagesWithInterval() {
        Observable.just("1").repeat(10);
        return RxResponse.sse(Observable.interval(2, TimeUnit.SECONDS).map(x-> "hello at "+ LocalDateTime.now()));
    }
}
```
