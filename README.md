# simpleJson

 [ ![Download](https://api.bintray.com/packages/pecpwee/simpleJson/simpleJson/images/download.svg) ](https://bintray.com/pecpwee/simpleJson/simpleJson/_latestVersion)


A Java library for serializing and deserializing Json

## Add dependency
```
compile 'com.pecpwee.lib.simplejson:library:1.1.0'
```

## Serializing
```
String s = new SimpleJson().toJson(ModelClass);
```

## Deserializing

```
ModelClass instance = new Gson().fromJson(str, ModelClass.class);
```
