import React, { useEffect, useState } from "react";
import ajax from "../Services/fetchService";
import { Button, Card, Col, Row } from "react-bootstrap";
import StatusBadge from "../StatusBadge";
import { useNavigate } from "react-router-dom";
import { useUser } from "../UserProvider";

const Dashboard = () => {
  const navigate = useNavigate();
  const user = useUser();
  const [assignments, setAssignments] = useState(null);

  const [searchTerm, setSearchTerm] = useState("");

  useEffect(() => {
    ajax("api/assignments", "GET", user.jwt).then((assignmentData) => {
      setAssignments(assignmentData);
    });
    if (!user.jwt) navigate("/login");
  }, [user.jwt]);

  function createAssignment() {
    ajax("api/assignments", "POST", user.jwt).then((assignment) => {
      navigate(`/assignments/${assignment.id}`);
      // window.location.href = `/assignments/${assignment.id}`;
    });
  }

  return (
    <div style={{ margin: "2em" }}>
      <Row>
        <Col>
          <span
            className="d-flex justify-content-end"
            style={{ cursor: "pointer" }}
            onClick={() => {
              user.setJwt(null);
            }}
          >
            Logout
          </span>
        </Col>
      </Row>
      <div className="mb-5">
        <Button size="lg" onClick={() => createAssignment()}>
          Submit New Assignment
        </Button>
      </div>
      <Row>
        <Col>
          <div className="search-box">
            <input
              type="text"
              placeholder="Search..."
              onChange={(event) => {
                setSearchTerm(event.target.value);
              }}
            />
          </div>
        </Col>
      </Row>
      {assignments ? (
        <div
          className="d-grid gap-5"
          style={{ gridTemplateColumns: "repeat(auto-fill, 18rem)" }}
        >
          {assignments
            .filter((val) => {
              if (searchTerm === "") return val;
              else if (val.githubUrl.toLowerCase().includes(searchTerm.toLowerCase())) return val;
              return false;
            })
            .map((assignment) => (
              <Card
                key={assignment.id}
                style={{ width: "18rem", height: "18rem" }}
              >
                <Card.Body className="d-flex flex-column justify-content-around">
                  <Card.Title>Assignment #{assignment.number}</Card.Title>
                  <div className="d-flex align-items-start">
                    <StatusBadge text={assignment.status} />
                  </div>
                  <Card.Text style={{ marginTop: "1em" }}>
                    <p>
                      <b>Github URL:</b> {assignment.githubUrl}
                    </p>
                    <p>
                      <b>Branch:</b> {assignment.branch}
                    </p>
                  </Card.Text>
                  <Button
                    variant="secondary"
                    onClick={() => {
                      navigate(`/assignments/${assignment.id}`);
                      // window.location.href = `/assignments/${assignment.id}`;
                    }}
                  >
                    Edit
                  </Button>
                </Card.Body>
              </Card>
            ))}
        </div>
      ) : (
        <></>
      )}
    </div>
  );
};

export default Dashboard;
