# simpleJson
A Java library for serializing and deserializing Json

## Add dependency
```
compile 'com.pecpwee.lib.simplejson:library:1.0.0'
```

## Serializing
```
String s = new SimpleJson().toJson(ModelClass);
```

## Deserializing

```
ModelClass instance = new Gson().fromJson(str, ModelClass.class);
```
