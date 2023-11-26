```shell
cd react-native-bundle

npx react-native@0.72.6 init AModel --version 0.72.6
npx react-native@0.72.6 init BModel --version 0.72.6
npx react-native@0.72.6 init host --version 0.72.6

cd host
npx react-native run-android

cd AModel
npx react-native@0.72.6 bundle --entry-file index.js --bundle-output ./AModel/AModel.bundle --platform android --assets-dest ./bundle --dev false

cd BModel
npx react-native@0.72.6 bundle --entry-file index.js --bundle-output ./BModel/BModel.bundle --platform android --assets-dest ./bundle --dev false
```

