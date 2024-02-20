FROM ubuntu:18.04
COPY ./sap-abap /sap-abap
COPY ./resources /resources
CMD ["bash"]
