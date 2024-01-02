/** @type {import('ts-jest').JestConfigWithTsJest} */
// eslint-disable-next-line no-undef
module.exports = {
  preset: 'ts-jest',
  testEnvironment: 'jsdom',
  moduleNameMapper: {
    '^.+\\.svg$': 'jest-svg-transformer',
    '^.+\\.(css|less|scss)$': 'identity-obj-proxy',
    '^@/(.*)$': '<rootDir>/src/$1',
    '^assets/(.*)$': '<rootDir>/src/assets/$1',
    '^components$': '<rootDir>/src/components',
    '^appConstants$': '<rootDir>/src/constants',
    '^hooks/(.*)$': '<rootDir>/src/hooks/$1',
    '^pages$': '<rootDir>/src/pages',
    '^providers$': '<rootDir>/src/providers',
    '^types/(.*)$': '<rootDir>/src/types/$1',
    '^utils$': '<rootDir>/src/utils'
  },
  setupFilesAfterEnv: ['<rootDir>/setupTests.ts'],
  collectCoverage: true,
  collectCoverageFrom: [
    'src/**/*.{tsx,ts}',
    '!src/{App,main}.tsx',
    '!src/**/{index,vite-env.d}.ts',
    '!src/**/types.ts',
    '!src/providers/**/*.{tsx,ts}'
  ],
  testTimeout: 25000
};
