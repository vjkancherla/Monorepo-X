Watch this playlist: https://www.youtube.com/watch?v=BZYj6yfA6Bs&list=PLdk2EmelRVLovmSToc_DK7F1DV_ZEljbx&index=1
https://bazel.build/concepts/build-ref

[1] Install Bazel by running:
brew install bazelisk

[2] Add ".bazelversion" file to the root of repo and specify the bazel version you want to use.

[3] Create WORKSPACE.bazel file at the root of repo.
WORKSPACE may be empty, or may contain references to external dependencies required to build the outputs.
External dependencies: Bazel supports external dependencies, source files (both text and binary) used in your build
that are not from your workspace. For example, they could be a ruleset hosted in a GitHub repo, a Maven artifact, or
a directory on your local machine outside your current workspace.

[4] BUILD Files: These files, named BUILD (or BUILD.bazel), are present in every directory of the project containing
source code you want Bazel to manage. They specify how the source code in the directory should be built and any dependencies it might have.
- add BUILD.bazel file to Monorepo-X/Microservices/Podinfo-Frontend-App/src/BUILD.bazel
- add BUILD.bazel file to Monorepo-X/Microservices/Python-App/src/BUILD.bazel


[5] Targets: These are buildable things. It could be a binary you want to produce, a library, or even a test you want to run.
They are specified in BUILD files.
EG: genrule(
    name = "foo", <---- target name
    outs = ["foo.txt"],
    cmd_bash = "sleep 5 && echo 'Hello World!!' >$@",
)

[6] Rules: Rules define how to go from inputs (like source files) to outputs (like executables or libraries).
Examples include cc_binary, cc_library, and cc_test for C/C++ projects.

EG: genrule( <---- RULE
    name = "foo",
    outs = ["foo.txt"], <--- output
    cmd_bash = "sleep 5 && echo 'Hello World!!' >$@", <--- action to take to generate output
)

[7] Invoking bazel:
[a] build all targets
{ROOT_of_Repo}>> bazel build //...

[b] build all targets only in Python-app
{ROOT_of_Repo}>> bazel build //Microservices/Python-App/src/...

[c] build a specific target, by name "foo"
{ROOT_of_Repo}>> bazel build //Microservices/Python-App/src:foo
