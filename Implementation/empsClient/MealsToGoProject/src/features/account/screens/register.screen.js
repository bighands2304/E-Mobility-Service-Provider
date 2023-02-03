import React, { useState, useContext } from "react";
import {
  AccountBackground,
  AccountCover,
  AccountContainer,
  AuthButton,
  AuthInput,
  ErrorContainer,
  Title,
} from "../components/account.style";
import { Text } from "../../../components/typography/text.component";
import { Spacer } from "../../../components/spacer/spacer.component";
import { AuthenticationContext } from "../../../services/authentication/authentication.context";
import { ActivityIndicator, Colors } from "react-native-paper";

export const RegisterScreen = ({ navigation }) => {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
  const [repeatedPassword, setRepeatedPassword] = useState("");

  const [name, setName] = useState("");
  const [surname, setSurname] = useState("");
  const [username, setUsername] = useState("");

  const { onRegister, isLoading, error } = useContext(AuthenticationContext);
  return (
    <AccountBackground>
      <AccountCover />
      <Title>E-MALL</Title>
      <AccountContainer>
        <AuthInput
          label="E-mail"
          value={email}
          textContentType="emailAddress"
          keyboardType="email-address"
          autoCapitalize="none"
          onChangeText={(u) => setEmail(u)}
        />
        <Spacer size="large">
          <AuthInput
            label="Password"
            value={password}
            textContentType="password"
            secureTextEntry
            autoCapitalize="none"
            onChangeText={(p) => setPassword(p)}
          />
        </Spacer>
        <Spacer size="large">
          <AuthInput
            label="Repeat Password"
            value={repeatedPassword}
            textContentType="password"
            secureTextEntry
            autoCapitalize="none"
            onChangeText={(p) => setRepeatedPassword(p)}
          />
        </Spacer>
        <Spacer size="large">
          <AuthInput
            label="username"
            value={username}
            textContentType="text"
            autoCapitalize="none"
            onChangeText={(p) => setUsername(p)}
          />
        </Spacer>
        <Spacer size="large">
          <AuthInput
            label="name"
            value={name}
            textContentType="text"
            autoCapitalize="none"
            onChangeText={(p) => setName(p)}
          />
        </Spacer>
        <Spacer size="large">
          <AuthInput
            label="surname"
            value={surname}
            textContentType="text"
            autoCapitalize="none"
            onChangeText={(p) => setSurname(p)}
          />
        </Spacer>
        {error && (
          <ErrorContainer size="large">
            <Text variant="error">{error}</Text>
          </ErrorContainer>
        )}
        <Spacer size="large">
          {!isLoading ? (
            <AuthButton
              icon="email"
              mode="contained"
              onPress={async () => {
                const result = await onRegister(
                  email,
                  password,
                  repeatedPassword,
                  username,
                  name,
                  surname
                );
                if (result === true) {
                  navigation.navigate("Login");
                } else {
                  console.log("b");
                }
              }}
            >
              Register
            </AuthButton>
          ) : (
            <ActivityIndicator animating={true} color={"#174a7a"} />
          )}
        </Spacer>
      </AccountContainer>
      <Spacer size="large">
        <AuthButton mode="contained" onPress={() => navigation.goBack()}>
          Back
        </AuthButton>
      </Spacer>
    </AccountBackground>
  );
};
