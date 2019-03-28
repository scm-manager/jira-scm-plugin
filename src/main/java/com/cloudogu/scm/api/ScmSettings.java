package com.cloudogu.scm.api;

public interface ScmSettings {

    String getRepositoryURL(String project);

    void setRepositoryURL(String project, String repositoryURL);

}
