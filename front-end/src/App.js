import { useEffect, useState } from "react";
import { Route, Routes } from "react-router-dom";
import "./App.css";
import Dashboard from "./Dashboard";
import Homepage from "./Homepage";
import { useLocalState } from "./util/useLocalStorage";
import Login from "./Login";
import PrivateRoute from "./PrivateRoute";
import AssignmentView from "./AssignmentView";
import "bootstrap/dist/css/bootstrap.min.css";
import jwt_decode from "jwt-decode";
import CodeReviewerDashboard from "./CodeReviewerDashboard";
import CodeReviewerAssignmentView from "./CodeReviewAssignmentView";
import { UserProvider, useUser } from "./UserProvider";
import Register from "./Register";

function App() {
  const [jwt, setJwt] = useLocalState("", "jwt");
  const [roles, setRoles] = useState([]);
  const user = useUser();

  useEffect(() => {
    setRoles(getRolesFromJWT());
  }, [user.jwt]);

  function getRolesFromJWT() {
    if (user.jwt) {
      const decodedJwt = jwt_decode(user.jwt);
      return decodedJwt.authorities;
    }
    return [];
  }

  return (
      <Routes>
        <Route
          path="/dashboard"
          element={
            roles.find((role) => role === "ROLE_CODE_REVIEWER") ? (
              <PrivateRoute>
                <CodeReviewerDashboard />
              </PrivateRoute>
            ) : (
              <PrivateRoute>
                <Dashboard />
              </PrivateRoute>
            )
          }
        />
        <Route
          path="/assignments/:assignmentId"
          element={
            roles.find((role) => role === "ROLE_CODE_REVIEWER") ? (
              <PrivateRoute>
                <CodeReviewerAssignmentView />
              </PrivateRoute>
            ) : (
              <PrivateRoute>
                <AssignmentView />
              </PrivateRoute>
            )
          }
        />
        <Route path="login" element={<Login />} />
        <Route path="/" element={<Homepage />} />
        <Route path="register" element={<Register/>} />
      </Routes>
  );
}

export default App;
