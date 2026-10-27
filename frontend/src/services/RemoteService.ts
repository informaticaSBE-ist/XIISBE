import axios from 'axios'
import { useAppearanceStore } from '@/stores/appearance'
import SBEError from '@/models/SBEError'
import type PersonDto from '@/models/PersonDto'

const httpClient = axios.create()
httpClient.defaults.timeout = 50000
httpClient.defaults.baseURL = import.meta.env.VITE_ROOT_API
httpClient.defaults.headers.post['Content-Type'] = 'application/json'

// All calls to the backend go through here.
export default class RemoteServices {
  // --- People ---
  static async getPeople(): Promise<PersonDto[]> {
    return httpClient.get('/people')
  }

  static async createPerson(person: PersonDto): Promise<PersonDto> {
    return httpClient.post('/people', person)
  }

  static async deletePerson(personId: number): Promise<void> {
    return httpClient.delete(`/people/${personId}`)
  }

  // --- Errors ---
  static async errorMessage(error: any): Promise<string> {
    if (error.message === 'Network Error') {
      return 'Unable to connect to the server'
    } else if (error.message.split(' ')[0] === 'timeout') {
      return 'Request timeout - Server took too long to respond'
    } else {
      return error.response?.data?.message ?? 'Unknown Error'
    }
  }

  static async handleError(error: any): Promise<never> {
    const sbeError = new SBEError(
      await RemoteServices.errorMessage(error),
      error.response?.data?.code ?? -1
    )
    const appearance = useAppearanceStore()
    appearance.pushError(sbeError)
    appearance.loading = false
    throw sbeError
  }
}

httpClient.interceptors.response.use((response) => response.data, RemoteServices.handleError)
