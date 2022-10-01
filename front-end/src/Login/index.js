import React, { useEffect, useState } from "react";
import { Button, Col, Container, Form, Row } from "react-bootstrap";
import { useNavigate } from "react-router-dom";
import { useUser } from "../UserProvider";

const Login = () => {
  const user = useUser();
  const navigate = useNavigate();
  const [username, setUsername] = useState("");
  const [password, setPassword] = useState("");

  useEffect(() => {
    if(user.jwt) navigate("/dashboard");
  }, [user]);

  function sendLoginRequest() {
    const reqBody = {
      username: username,
      password: password,
    };

    fetch("api/auth/login", {
      headers: {
        "Content-Type": "application/json",
      },
      method: "post",
      body: JSON.stringify(reqBody),
    })
      .then((response) => {
        if (response.status === 200)
          return Promise.all([response.json(), response.headers]);
        else return Promise.reject("Invalid login attempt");
      })
      .then(([body, headers]) => {
        console.log("Logged");
        user.setJwt(headers.get("authorization"));
      })
      .catch((message) => {
        alert(message);
      });
  }

  return (
    <>
      <Container className="mt-5">
        <Row className="justify-content-center">
          <Col md="6" lg="6">
            <Form.Group className="mb-3" controlId="username">
              <Form.Label className="fs-4">Username</Form.Label>
              <Form.Control
                type="email"
                size="lg"
                placeholder="joe@gmail.com"
                value={username}
                onChange={(e) => setUsername(e.target.value)}
              />
            </Form.Group>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col md="6" lg="6">
            <Form.Group className="mb-3" controlId="password">
              <Form.Label className="fs-4">Password</Form.Label>
              <Form.Control
                type="password"
                size="lg"
                placeholder="Type in your password"
                value={password}
                onChange={(e) => setPassword(e.target.value)}
              />
            </Form.Group>
          </Col>
        </Row>
        <Row className="justify-content-center">
          <Col
            md="6"
            lg="6"
            className="mt-3 d-flex flex-column gap-5 flex-md-row justify-content-between"
          >
            <Button
              id="submit"
              size="lg"
              type="button"
              onClick={() => sendLoginRequest()}
            >
              Login
            </Button>
            <Button
              variant="secondary"
              size="lg"
              type="button"
              onClick={() => {
                navigate("/");
              }}
            >
              Exit
            </Button>
          </Col>
        </Row>
      </Container>
    </>
  );
};

export default Login;
