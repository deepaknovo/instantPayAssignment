import React, {useState, useEffect} from 'react';

import {
  NativeModules,
  SafeAreaView,
  ScrollView,
  StatusBar,
  StyleSheet,
  Dimensions,
  Text,
  TouchableOpacity,
  useColorScheme,
  View,
} from 'react-native';
import base64 from 'react-native-base64';
import PickerComponent from './resources/ImagePickerComponent';
import {apiCalling, getData, getDec} from './resources/CommanFunction';

const App = () => {
  const [data, setData] = useState({});
  const [dec, setDec] = useState('');
  const [textToEncrypt, setTextToEncrypt] = useState(
    'This is some sample plaintext data to encrypt for insta pay',
  );

  return (
    <SafeAreaView style={{flex: 1}}>
      <ScrollView contentInsetAdjustmentBehavior="automatic">
        <View>
          <Text style={styles.sectionDescription}>
            TEXT TO ENCRYPT:
            <Text style={{color: 'blue'}}>{textToEncrypt}</Text>
          </Text>
          <Text style={styles.sectionDescription}>
            ENCRYPTED DATA:
            <Text style={{color: 'blue'}}>{data.cipher}</Text>
          </Text>
          <Text style={styles.sectionDescription}>
            IV: <Text style={{color: 'blue'}}>{data.iv}</Text>
          </Text>
          <Text style={styles.sectionDescription}>
            Decrypted: <Text style={{color: 'blue'}}>{dec}</Text>
          </Text>
          <TouchableOpacity
            onPress={() => getData(textToEncrypt).then(res => setData(res))}>
            <Text style={styles.button}> Click to Encrypt</Text>
          </TouchableOpacity>
          <TouchableOpacity
            onPress={() => getDec(data).then(res => setDec(res))}>
            <Text style={styles.button}>Click to Decrypt</Text>
          </TouchableOpacity>
          <TouchableOpacity
            onPress={
              () => {
                apiCalling(base64.encode(data?.iv ?? '' + data?.cipher ?? ''));
              }

              // apiCalling('BbkMgp/humXXXxK42Y/m/Zl9d2+pbq+AbOjqQrVFovM=')
            }>
            <Text style={styles.button}> Click to hit API</Text>
          </TouchableOpacity>
        </View>

        <View style={{height: Dimensions.get('window').height / 2}}>
          <PickerComponent token="" />
        </View>
      </ScrollView>
    </SafeAreaView>
  );
};

const styles = StyleSheet.create({
  sectionContainer: {
    marginTop: 32,
    paddingHorizontal: 24,
  },
  sectionTitle: {
    fontSize: 24,
    fontWeight: '600',
  },
  sectionDescription: {
    fontWeight: 'bold',
    marginVertical: 8,
    fontSize: 18,
    fontWeight: '400',
    paddingHorizontal: 10,
  },
  highlight: {
    fontWeight: '700',
  },
  button: {
    textDecorationLine: 'underline',
    padding: 20,
    textAlign: 'center',
  },
});

export default App;
