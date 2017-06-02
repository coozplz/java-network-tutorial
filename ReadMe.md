# 자바 네트워크 프로그래밍

`자바 네트워크 프로그래밍` 강의를 위한 저장소로 하위 프로젝트에는 아래와 같은 프로젝트를 포함하고 있다.

1. 블럭킹(Blocking I/O)
    1. 에코 클라이언트
    2. 에코 서버
    3. 멀티 스레드 에코 서버
    4. 채팅 클라이언트
    5. 멀티 스레드 채팅 서버
2. NIO(New I/O)
    1. 에코 클라이언트
    2. 에코 서버
    3. 채팅 클라이언트
    4. 채팅 서버
3. Netty
    1. Blocking IO
        1. 에코 클라이언트
        2. 에코 서버
    2. Non-Blocking IO
        1. 에코 클라이언트
        2. 에코 서버
        3. 채팅 클라이언트
        4. 채팅 서버
    
4. 공통
    1. 채팅 프로토콜 


## Java 스트림(Stream)

### Stream

자바에서 사용하는 `Stream`의 목표는 입출력을 위한 다양한 방법을 추상화하는 것이다. `Stream`은 대상이 파일, 네트워크, 화면인지는 중요하지 않다. 중요한 것은 `Stream`에서 정보를 받거나 `Stream`으로 정보를 보내는 것이다. 

### InputStream

네트워크 또는 파일등에서 정보를 읽어오는 역할을 한다.

#### 중요 메소드

1. `read()`: `Stream` 으로부터 한 `Byte`를 읽어서 결과를 숫자로 반환 읽은 데이터가 없는 경우 -1을 반환한다.

2. `read(byte[])`: 바이트의 배열만큼 입력정보를 읽고 몇 바이트를 읽었는지를 반환한다.

3. `read(byte[], int start, int len)` : 주어진 배열에 처음위치(start)부터 지정된 길이(len) 까지 `Stream`을 읽는다. 읽혀진 데이터는 바이트 배열에 저장되고 몇 바이트를 읽었는지 반환한다.

4. `close()`: `InputStream` 을 닫는다. `Stream`을 닫게 되면 다시 읽을 수 없다. 사용이 완료된 `Stream`은 반드시 닫아야 `메모리 누수(Memory Leak)`를 예방할 수 있다.


### OutputStream

네트워크 또는 파일등에 정보를 보내는 역할을 한다.

#### 중요 메소드

1. `write(int value)`: 값을 보낸다.
2. `write(byte[] bytes)`: 바이트의 배열을 스트림을 통해 보낸다.
3. `write(byte[] bytes, int start, int len)`: 바이트 배열의 시작위치(start)에서 길이(len)만큼 보낸다.



### Reader

`Stream`은 기본적으로 `Byte`를 통한 통신을 하는데 `Reader`를 통해 `Text`형태로 정보를 읽는다.


#### InputStreamReader

`Byte` 형태의 정보를 `Text`로 정보를 읽는다.

#### OutputStreamWriter

`Text` 형태의 정보를 `Byte`로 보낸다.

### BufferedReader / BufferedWriter

자바에서는 기본적으로 읽기 / 쓰기 동작을 한 `Byte` 단위로 진행한다. 

예를 들어 아래의 코드는 연결된 대상에서 하나의 `Byte` 를 읽어온다. 아래의 작업은 정상동작을 하지만 *효율적*이지 못하다. 그렇기 때문에 `Buffer` 를 사용하여 보다 효율적으로 정보를 읽어오도록 하는 역할을 한다.

```
InputStream is = connection.openStream();
int read = is.read();
```



## 블럭킹 I/O(Blocking I/O)

자바의 `java.net` 패키지에서 제공하는 `Socket` 클래스를 활용하여 네트워크를 사용하는 프로그램을 제작하며 `Socket` 프로그램을 어떻게 제작하는지 그리고 주의사항등을 알아본다.


## NIO(New I/O)

자바에서 제공하는 `java.nio` 패키지에서 제공하는 `SocketChannel` 클래스를 활용하여 네트워크를 사용하는 프로그램을 제작하며 `NIO` 프로그래밍을 어떻게 제작하는지 그리고 주의사항등을 알아본다.

## Netty

고성능 네트워크 프로그램을 효율적으로 제작하기 위한 `Netty` 프레임워크에 대한 사용법을 알아보고 제작해본다.

