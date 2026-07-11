export interface User {
  id?: number;
  username: string;
  email: string;
  role: 'ADMIN' | 'STAFF' | 'STUDENT';
}

export interface Class {
  id: number;
  className: string;
  stream?: string;
}

export interface Subject {
  id: number;
  subjectName: string;
  subjectCode: string;
}

export interface Staff {
  id: number;
  firstName: string;
  lastName: string;
  department: string;
  position: string;
}
