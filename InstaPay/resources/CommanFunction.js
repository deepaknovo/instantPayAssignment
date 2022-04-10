import {NativeModules} from 'react-native';

// var Aes = NativeModules.Aes;
import Aes from 'react-native-aes-crypto';
let localKey =
  'ab821eb4b7d352cd65e84c5a7f38dbb0966262c651cf7064a0d821d8b2a20a5a';
// let localKey = 'ab821eb4b7d352cd';

const generateKey = (password, salt, cost, length) =>
  Aes.pbkdf2(password, salt, cost, length);

const encrypt = (text, key) => {
  // creating a random string of length 16 and using it as Initialization Vector
  // returning object having encrypted text as well as iv

  return Aes.randomKey(16).then(iv => {
    return Aes.encrypt(text, key, iv, 'aes-128-cbc').then(cipher => ({
      cipher,
      iv,
    }));
  });
};
const decrypt = async (encryptedData, key) => {
  // using the encryptedData,iv recieved from the encryption method
  // console.log({encryptedData, key});
  return Aes.decrypt(
    encryptedData.cipher,
    key,
    encryptedData.iv,
    'aes-128-cbc',
  ).catch(e => console.log(e, 'INSIDE'));
};

const getData = async str => {
  // return generateKey('DEMO', 'salt', 5000, 128).then(key => {
  //   return encrypt(str, key)
  //     .then(({cipher, iv}) => {
  //       console.log('Encrypted:', cipher);
  //       console.log('Encryptedic:', iv);

  //       return {cipher, iv, key};
  //     })
  //     .catch(error => {
  //       console.error(error);
  //     });
  // });
  return encrypt(str, localKey)
    .then(({cipher, iv}) => {
      console.log('Encrypted:', cipher);
      console.log('Encryptedic:', iv);

      return {cipher, iv};
    })
    .catch(error => {
      console.error(error);
    });
};

const getDec = async data => {
  var {cipher, iv} = data;
  return decrypt({cipher, iv}, localKey).then(decrypted => {
    console.log({decrypted});
    return decrypted;
  });
};

//api functions

async function apiCalling(str) {
  console.log(str);

  let request = {ciphertext: str};

  fetch('https://www.instantpay.in/ws/AndroidRecruitmentTest/getToken', {
    method: 'POST', // or 'PUT'
    headers: {
      'Content-Type': 'application/json',
      Accept: 'application/json',
    },
    body: JSON.stringify(request),
  })
    .then(response => response.json())
    .then(data => {
      console.log('Success:', data);
    })
    .catch(error => {
      console.error('Error:', error);
    });
}

export {getData, getDec, apiCalling};
