#FROM openjdk:24-slim
FROM openjdk:17-alpine

ARG WORK_DIR=/opt/user-mgmt-svc
ARG ARTIFACT_NAME=acma-usermgmt-service.jar
ARG SERVICE_PORT=5051

ENV FINAL_ARTIFACT_NAME=${WORK_DIR}/${ARTIFACT_NAME}

RUN mkdir ${WORK_DIR}

WORKDIR ${WORK_DIR}

COPY target/${ARTIFACT_NAME} ${WORK_DIR}

EXPOSE ${SERVICE_PORT}

CMD [ "sh", "-c", "java -jar ${FINAL_ARTIFACT_NAME}" ]
