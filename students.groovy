class Course {

  def name
  def grades = []

  Course(def name) {
     this.name = name
  }
 
  void addGrade(float grade) {
    this.grades.add(grade)
  }

  void total() {
    def count = this.grades.size()
    def total = this.grades.stream().reduce(0, (prev, curr) -> prev + curr)
    def avg = (total / count)
    printf("%s - %d exams - %3.2f avg\n", this.name, count, avg)
  }
}

class Student {

  def name
  def courses = []

  Student(def name) {
     this.name = name
  }
 
  void addGrade(def course, def grade) {
    def c = this.courses.find((c) -> c.name == course)
    if (!c) {
      c = new Course(course)
      this.courses.push(c)
    }

    c.addGrade(grade)
  }

  def totals() {
    printf("Grades for %s\n", this.name)
    this.courses.forEach(c -> {      
      c.total()
    })
  }
}

void help() {
  println("Grading system")
  println("add-student [name] - Add student")
  println("select-student [name] - Select a student")
  println("add-grade [course] [grade] - Add a grade for a course for the current selected student")
  println("end - Stop the program")
}

def command = null
def students = []
def student = null

while (true) {

  command = System.console().readLine 'Input command:'

  if (command == null) {
    println('Please enter a valid command')
    help()
  } else if (command == 'help') {
    help()
  } else if (command.startsWith('add-student')) {
    def name = command.substring(11).trim()
    student = new Student(name)
    students.add(student)
  } else if (command.startsWith('select-student')) {
    def name = command.substring(14).trim()
    student = students.find((s) -> s.name == name)
    if (student) {
      printf("Student %s is selected", name)
    } else {
      printf("Student %s not found", name)
    }
  } else if (command.startsWith('add-grade')) {
    def arg = command.substring(9).trim()
    def args = arg.split(' ')
    if (student) {
      student.addGrade(args[0], Float.valueOf(args[1]))
    } else {
      println('No student is selected')
    }
  } else if (command == 'end' || command == 'quit') {
    break
  }
}

students.forEach(s -> {
  s.totals()
})