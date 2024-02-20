# docker build --progress=plain -f sap-abap/Dockerfile --no-cache .
FROM ubuntu:18.04
COPY ./sap-abap /sap-abap
CMD ["bash"]