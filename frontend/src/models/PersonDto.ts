// Same fields as the backend's PersonDto.java
export default class PersonDto {
  id?: number
  name?: string
  email?: string

  constructor(obj?: Partial<PersonDto>) {
    Object.assign(this, obj)
  }
}
