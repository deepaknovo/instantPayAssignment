import {
  StyleSheet,
  Text,
  View,
  Dimensions,
  Platform,
  Image,
  TouchableOpacity,
  Modal,
  Alert,
} from 'react-native';
import React, {useState} from 'react';
import {
  ImagePickerResponse,
  launchCamera,
  launchImageLibrary,
  MediaType,
} from 'react-native-image-picker';
import {useEffect} from 'react/cjs/react.development';

export default function PickerComponent(props) {
  useEffect(() => {
    console.log(props);
  }, [props]);

  const [modalVisible, setModalVisible] = useState(false);
  const [fileData, setFileData] = useState();
  useEffect(() => {
    console.log(fileData);
    if (fileData != null && setModalVisible) {
      setModalVisible(false);
    }
  }, [fileData]);

  function showAlert() {
    if (Platform.OS === 'ios') {
      Alert.alert('Upload Photo', '', [
        {text: 'Take Photo', onPress: () => openCameraWithPermission()},
        {text: 'Choose from Gallery', onPress: () => chooseFile('photo')},
        {text: 'Cancel', onPress: () => console.log('canceled')},
      ]);
    } else {
      setModalVisible(true);
    }
  }

  const openCameraWithPermission = async () => {
    try {
      launchCamera(
        {
          mediaType: 'photo',
          includeBase64: false,
          maxWidth: 300,
          maxHeight: 550,
        },
        response => {
          if (response.didCancel) {
            return;
          } else if (response.errorCode == 'camera_unavailable') {
            Alert.alert('Camera not available on device');
            return;
          } else if (response.errorCode == 'permission') {
            return;
          } else if (response.errorCode == 'others') {
            Alert.alert(response.errorMessage ?? '');
            return;
          }
          setFileData(response);
        },
      );
    } catch (err) {
      console.warn(err);
    }
  };
  const chooseFile = type => {
    let options = {
      mediaType: type,
      maxWidth: 300,
      maxHeight: 550,
    };
    launchImageLibrary(options, response => {
      console.log('Response = ', response);

      if (response.didCancel) {
        return;
      } else if (response.errorCode == 'permission') {
        return;
      } else if (response.errorCode == 'others') {
        Alert.alert(response.errorMessage ?? '');
        return;
      }
      setFileData(response);
    });
  };

  function serverHitForUploadImage() {
    let data = fileData?.assets?.[0];
    let uri =
      Platform === 'ios' ? data?.uri?.replace('file://', '') : data?.uri;
    let photo = {
      uri: uri,
      type: 'image/jpeg',
      name: fileData?.fileName ?? '',
    };

    let request = new FormData();

    if (uri == undefined || uri == '' || uri == null) {
      Alert.alert('Select Image');
      return;
    } else {
      request.append('image', photo);
    }

    if (props.token == '') {
      Alert.alert('Alert', 'Not allowed to upload the image (invalid token)');
      return;
    }
    fetch('https://www.instantpay.in/ws/AndroidRecruitmentTest/uploadImage', {
      method: 'POST', // or 'PUT'
      headers: {
        token: props?.token ?? '',
        Accept: 'application/json',
        'Content-Type': 'multipart/form-data',
      },
      body: request,
    })
      .then(response => response.json())
      .then(data => {
        console.log('Success:', data);
      })
      .catch(error => {
        console.error('Error:', error);
      });
  }

  return (
    <View style={{flex: 1, borderWidth: 1, borderColor: 'lightgray'}}>
      <Text style={{padding: '2%'}}>Upload Image</Text>
      <TouchableOpacity
        onPress={() => {
          showAlert();
        }}
        style={{
          alignSelf: 'center',
          marginVertical: 20,
          borderWidth: 1,
          borderColor: 'gray',
          borderRadius: 10,
          padding: '1%',
        }}>
        <Text>Select Image</Text>
      </TouchableOpacity>

      {fileData?.assets[0]?.uri ? (
        <Image
          resizeMode="contain"
          source={{uri: fileData?.assets[0]?.uri ?? ''}}
          style={{padding: '5%', height: '50%', width: '100%'}}></Image>
      ) : null}
      <TouchableOpacity
        onPress={() => {
          serverHitForUploadImage();
        }}
        style={{
          alignSelf: 'center',
          marginVertical: 20,
          borderWidth: 1,
          borderColor: 'gray',
          borderRadius: 10,
          padding: '1%',
        }}>
        <Text>Upload Image</Text>
      </TouchableOpacity>
      <Modal animationType="fade" transparent={true} visible={modalVisible}>
        <View style={styles.centeredView}>
          <View style={styles.modalView}>
            <View style={styles.Header}>
              <Text style={styles.modalText}>Upload Photo</Text>
            </View>
            <View style={styles.mainBody}>
              <TouchableOpacity
                style={styles.alertText}
                onPress={() => openCameraWithPermission()}>
                <Text>Take Photo</Text>
              </TouchableOpacity>
              <TouchableOpacity
                style={styles.alertText}
                onPress={() => chooseFile('photo')}>
                <Text>Choose from gallery</Text>
              </TouchableOpacity>

              <TouchableOpacity
                style={styles.alertText}
                onPress={() => setModalVisible(!modalVisible)}>
                <Text>Cancel</Text>
              </TouchableOpacity>
            </View>
          </View>
        </View>
      </Modal>
    </View>
  );
}

const styles = StyleSheet.create({
  modalView: {
    marginTop: 200,
    backgroundColor: 'lightgray',
    alignSelf: 'center',
    // backgroundColor: 'white',
    borderRadius: 5,
    shadowColor: '#000',
    shadowOffset: {
      width: 0,
      height: 2,
    },
    shadowOpacity: 0.25,
    shadowRadius: 4,
    elevation: 5,
    width: Dimensions.get('window').width / 1.6,
    paddingBottom: 10,
  },
  modalText: {
    fontFamily: 'Poppins-Medium',
    fontSize: 16,
  },
  mainBody: {
    paddingHorizontal: 20,
    alignItems: 'flex-end',
  },
  alertText: {
    fontSize: 18,
    fontFamily: 'Poppins-Regular',
    paddingBottom: 15,
  },
});
