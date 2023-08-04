#!python3

import os
import subprocess

test_cases_dir = ".\\testcases\\sema\\"
# compile_command = "bash ./build.bash"
execute_command = "D:\\Java\\bin\\java.exe \"-javaagent:D:\\IntelliJ IDEA 2023.1.3\\lib\\idea_rt.jar=27721:D:\\IntelliJ IDEA 2023.1.3\\bin\" -Dfile.encoding=UTF-8 -Dsun.stdout.encoding=UTF-8 -Dsun.stderr.encoding=UTF-8 -classpath D:\\compiler\\MxCompiler\\out\\production\\MxCompiler;C:\\Users\\李心瑶\\Downloads\\antlr-4.13.0-complete.jar Main"
test_start = ""

def collect_test_cases():
  test_cases = []
  for dir in os.listdir(test_cases_dir):
    if os.path.isdir(test_cases_dir + dir):
      for f in os.listdir(test_cases_dir + dir):
        if os.path.splitext(f)[1] == '.mx':
          if os.path.splitext(f)[0] == test_start:
            test_cases = []
          test_cases.append(dir + '\\' + f)
  test_cases.sort()
  return test_cases

def main():
  test_cases = collect_test_cases()
  for test_name in test_cases:
    print(test_cases_dir + test_name)
    input = open(test_cases_dir + test_name, 'r')
    expected = ""
    comment = ""
    try:
      line_count = 0
      while line_count < 10:
        line_count += 1
        line = input.readline()
        pos = line.find("Verdict:")
        # print(line)
        if pos != -1:
          expected = line[pos + 8:].strip()
          if (expected == "Success"):
            break
        pos = line.find("Comment:")
        if pos != -1:
          comment = line[pos + 8:].strip()
          break
    finally:
      input.close()

    str = execute_command + " < " + test_cases_dir + test_name
    status, result = subprocess.getstatusoutput(str)
    print("Running test " + test_name + ":")
    print("  " + "Expected: " + expected + " " + comment)
    print("  " + "Result:", "Success" if status == 0 else "Fail", result)
    expected_status = False if expected == "Success" else True
    if bool(status) != expected_status:
      print("Test fail at " + test_name + ", source code has been copied to ./src/test/mx/input.mx")
      return
  print("All tests passed!")


if __name__ == "__main__":
  if len(os.sys.argv) > 1:
    test_start = os.sys.argv[1]
  main()