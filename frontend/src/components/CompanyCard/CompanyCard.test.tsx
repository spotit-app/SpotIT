import { ReadCompany } from 'types/company';
import { act, render, screen } from '@testing-library/react';
import { CompanyCard } from '.';
import { RouterProvider } from 'providers';

describe('CompanyCard', () => {
  test('renders correctly', () => {
    const company: ReadCompany = {
      id: '1',
      name: 'TestName',
      logoUrl: 'test.png',
      websiteUrl: 'https://test.com',
      nip: '1234567890',
      regon: '1234567890',
      address: {
        id: '1',
        street: 'TestStreet',
        city: 'TestCity',
        zipCode: '11-111',
        country: 'TestCountry'
      }
    };

    act(() =>
      render(
        <RouterProvider>
          <CompanyCard company={company} />
        </RouterProvider>
      )
    );

    expect(screen.getByText('TestName')).toBeInTheDocument();
    expect(screen.getByText('NIP: 1234567890')).toBeInTheDocument();
  });
});
